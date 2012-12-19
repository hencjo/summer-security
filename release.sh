#!/bin/bash -e
VERSION=`git describe`
cat pom.xml | sed -e "s/1-NO-VERSION-SNAPSHOT/$VERSION/g"  > tmp-release.pom
mvn -f tmp-release.pom clean package
cp tmp-release.pom target/summer-security-$VERSION.pom
ARTIFACTS=("summer-security-$VERSION.pom" "summer-security-$VERSION.jar" "summer-security-$VERSION-sources.jar" "summer-security-$VERSION-javadoc.jar")

pushd target/ > /dev/null

GENERATED=()
for i in "${ARTIFACTS[@]}"
do
   :
   gpg --use-agent --armor --detach-sign $i
   GENERATED+=("$i.asc")
done
jar -cf summer-security-$VERSION-bundle.jar ${ARTIFACTS[*]} ${GENERATED[*]}
popd > /dev/null

rm tmp-release.pom
echo "Built target/summer-security-$VERSION-bundle.jar"
