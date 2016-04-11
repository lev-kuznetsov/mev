BASEDIR=$(dirname "$0")
echo "$BASEDIR"
ln -s `readlink -f $BASEDIR/src/main/javascript/edu/dfci/cccb/mev/web/ui` $BASEDIR/target/classes/edu/dfci/cccb/mev/web/ui
ln -s `readlink -f $BASEDIR/src/main/javascript/edu/dfci/cccb/mev/web/libs` $BASEDIR/target/classes/edu/dfci/cccb/mev/web/libs
ln -s `readlink -f $BASEDIR/src/main/javascript/edu/dfci/cccb/mev/web/mock` $BASEDIR/target/classes/edu/dfci/cccb/mev/web/mock
ln -s `readlink -f $BASEDIR/src/main/javascript/edu/dfci/cccb/mev/web/vendor` $BASEDIR/target/classes/edu/dfci/cccb/mev/web/vendor
ln -s `readlink -f $BASEDIR/src/main/javascript/edu/dfci/cccb/mev/web/javascript` $BASEDIR/target/classes/edu/dfci/cccb/mev/web/javascript

