import javax.inject._

import com.github.enalmada.filters.HttpsRedirectFilter
import play.api._
import play.api.http.HttpFilters

/**
  * This class configures filters that run on every request. This
  * class is queried by Play to get a list of filters.
  *
  * Play will automatically use filters from any class called
  * `Filters` that is placed the root package. You can load filters
  * from a different class by adding a `play.http.filters` setting to
  * the `application.conf` configuration file.
  *
  * @param env                 Basic environment settings for the current application.
  * @param httpsRedirectFilter A filter that redirects http to https.
  */
@Singleton
class Filters @Inject()(env: Environment,
                        httpsRedirectFilter: HttpsRedirectFilter) extends HttpFilters {

  override val filters = Seq(httpsRedirectFilter)

}

/*
import javax.inject.Inject

import akka.stream.Materializer
import com.mohiva.play.htmlcompressor.HTMLCompressorFilter
import play.api.Mode
import play.api.http.HttpFilters
import play.api.mvc._
import play.filters.cors.CORSFilter
import play.filters.csrf.CSRFFilter
import play.filters.headers.SecurityHeadersFilter

import scala.concurrent.{ExecutionContext, Future}


class Filters @Inject()(corsFilter: CORSFilter, csrfFilter: CSRFFilter, securityHeadersFilter: SecurityHeadersFilter, htmlCompressorFilter: HTMLCompressorFilter, tlsFilter: TLSFilter) extends HttpFilters {
  // not adding htmlComrpessor as it makes some subdomain forum links // which goes to https
  override def filters: Seq[EssentialFilter] = Seq(tlsFilter, csrfFilter, securityHeadersFilter)
}
*/