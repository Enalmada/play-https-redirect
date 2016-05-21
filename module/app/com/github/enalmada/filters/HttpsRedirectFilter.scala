package com.github.enalmada.filters

import javax.inject.Inject

import akka.stream.Materializer
import play.api.Logger
import play.api.mvc.{Filter, RequestHeader, Result, Results}

import scala.concurrent.{ExecutionContext, Future}

class HttpsRedirectFilter @Inject()(implicit val mat: Materializer, ec: ExecutionContext, env: play.api.Environment, config: play.api.Configuration) extends Filter {

  def isEnabled = !config.getBoolean("httpsRedirect.enabled").getOrElse(false)

  // request.secure doesn't seem to factor in x-forwarded-proto on AWS so doing this
  def isRequestSecure(request: RequestHeader) = request.secure || request.headers.get("X-Forwarded-Proto").map(_.toLowerCase).contains("https")

  def isMode = config.getStringList("httpsRedirect.modes").exists(_.contains(env.mode.toString))

  def isExcluded(currentUri: String): Boolean = config.getStringList("httpsRedirect.excluded").exists(_.contains(currentUri))

  // Don't turn on HSTS unless you are completely certain you never want to go back to http
  val hstsHeaderValue: String = {
    val maxAge = config.getLong("httpsRedirect.hsts.maxAge").getOrElse(31536000)

    // Don't turn this to true unless you are certain none of your subdomains will ever need to be non-https
    val includeSubDomains = config.getBoolean("httpsRedirect.hsts.includeSubDomains").getOrElse(false) match {
      case true => "includeSubDomains;"
      case false => ""
    }

    // Recommended: If the site owner would like their domain to be included in the HSTS preload list maintained by Chrome (and used by Firefox and Safari),
    // https://hstspreload.appspot.com/
    // the `preload` flag indicates the site owner's consent to have their domain preloaded. The site owner still needs to then go and submit the domain to the list.
    val preload = config.getBoolean("httpsRedirect.hsts.preload").getOrElse(false) match {
      case true => "preload;"
      case false => ""
    }

    s"max-age=$maxAge; $includeSubDomains $preload"
  }

  def apply(nextFilter: RequestHeader => Future[Result])
           (requestHeader: RequestHeader): Future[Result] = {

    // Only apply filter when it is explicitly turned on, using the proper mode, and current path is not excluded
    if (isEnabled && isMode && !isExcluded(requestHeader.uri)) {
      if (!isRequestSecure(requestHeader)) {
        val moveTo = "https://" + requestHeader.host.replace(config.getString("http.port").getOrElse("9000"), config.getString("https.port").getOrElse("9443")) + requestHeader.uri
        Future.successful(Results.MovedPermanently(moveTo))
      } else {

        config.getBoolean("httpsRedirect.hsts.enabled").getOrElse(false) match {
          case true => nextFilter(requestHeader).map(_.withHeaders("Strict-Transport-Security" -> hstsHeaderValue))
          case false => nextFilter(requestHeader)
        }


      }
    } else {
      nextFilter(requestHeader)
    }


  }

}