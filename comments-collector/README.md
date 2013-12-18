# Comments collector #

## Install MongoDB

### Linux

$ sudo apt-get install mongodb

## Build & Run ##

```sh
$ cd comments_collector
$ ./sbt
> container:start
> browse

If `browse` doesn't launch your browser, manually open [http://localhost:8080/](http://localhost:8080/) in your browser.
