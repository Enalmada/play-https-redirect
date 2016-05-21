# play-https-redirect [![Build Status](https://travis-ci.org/Enalmada/play-https-redirect.svg?branch=master)](https://travis-ci.org/Enalmada/play-https-redirect) [![Join the chat at https://gitter.im/Enalmada/play-https-redirect](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/Enalmada/play-https-redirect?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.enalmada/play-https-redirect/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.enalmada/play-https-redirect)

Playframework filter to redirect http to https.  I found myself needing the same code over and over in multiple projects so I decided to keep it DRY.

#### Version information
* `2.5.0` to `2.5.x` (last: `1.0.2` - [master branch](https://github.com/enalmada/play-https-redirect/tree/master))

Releases are on [mvnrepository](http://mvnrepository.com/artifact/com.github.enalmada) and snapshots can be found on [sonatype](https://oss.sonatype.org/content/repositories/snapshots/com/github/enalmada).

## Quickstart
Clone the project and run `sbt run` to see a sample application.
To test locally, add -Dhttps.port=9443 in your JAVA_OPTS or SBT_OPTS env
Don't turn on hsts unless you are 100% sure you never want to go back to http.


```
## HTTP to HTTPS redirect filter settings
httpsRedirect {
  modes=["Prod", "Dev"]
  excluded=["/health"]
  hsts {
    enabled=true
    maxAge=31536000
    preload = true
    includeSubDomains = true
  }
}
```

### Including the Dependencies

```xml
<dependency>
    <groupId>com.github.enalmada</groupId>
    <artifactId>play-https-redirect_2.11</artifactId>
    <version>1.0.2</version>
</dependency>
```
or

```scala
val appDependencies = Seq(
  "com.github.enalmada" %% "play-https-redirect" % "1.0.2"
)
```

## Versions
* 1.0.2 Initial release


## TODO
* only filter text types
* try and get it into playframework


## License

Copyright (c) 2016 Adam Lane

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with the License. You may obtain a copy of the License in the LICENSE file, or at:

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

