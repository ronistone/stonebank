VERSION=$1

if [ -z "${VERSION}" ]
  then
    echo "Informe a versão"
else
    mvn clean install -T4
    docker build -t ronistone/stonebank:${VERSION} .
    docker push ronistone/stonebank:${VERSION}

    kubectl patch deployment stonebank --patch "{ \"spec\": { \"template\": { \"spec\": { \"containers\": [{ \"name\": \"stonebank\", \"image\": \"ronistone/stonebank:${VERSION}\" }] } } } }"

    echo "${VERSION} Deployada!"
fi

