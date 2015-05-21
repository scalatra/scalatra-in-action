# run 'sbt test' on every project

set -e

# Run the test suite for each project.
for SCRIPT in `find . -name sbt -type f -perm -u=x`
do
    OLD_PWD=`pwd`
    PROJ=`dirname $SCRIPT`
    echo '########################################################'
    echo "BUILDING $PROJ                                          "
    echo '########################################################'
    cd $PROJ
    ./sbt test
    cd $OLD_PWD
done
