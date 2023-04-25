[![Maven Central](https://maven-badges.herokuapp.com/maven-central/foundation.icon/javaee-scorex/badge.svg)](https://search.maven.org/search?q=g:foundation.icon%20a:javaee-scorex)

# scorex package for Java SCORE

This repository contains some useful Java classes that can be used as substitutes for some Java standard I/O and collections frameworks when you write Java SCORE.
All classes are under `scorex` package namespace and can be mapped to the corresponding standard ones, e.g., you can use `scorex.util.ArrayList` instead of `java.util.ArrayList`.

## Usage

You can include this package from [Maven Central](https://search.maven.org/search?q=g:foundation.icon%20a:javaee-scorex)
by adding the following dependency in your `build.gradle`.

```groovy
implementation 'foundation.icon:javaee-scorex:0.5.4'
```

## License

The code is custom modification of the OpenJDK source to support the sandbox environment,
thus the code follows the OpenJDK license, the GNU General Public License version 2 ("GPLv2").
