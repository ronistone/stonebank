VERSION=$1


push_and_deploy() {
      docker push ronistone/${1}:${2}
      kubectl patch deployment ${1} --patch "{ \"spec\": { \"template\": { \"spec\": { \"containers\": [{ \"name\": \"${1}\", \"image\": \"ronistone/${1}:${2}\" }] } } } }"
}

if [ -z "${VERSION}" ]
  then
    echo "Informe a versão"
else
    mvn clean install -T4
    docker build -t ronistone/stonebank-application:${VERSION} ./application
    docker build -t ronistone/stonebank-consumer:${VERSION} ./consumer
    docker build -t ronistone/stonebank-processor:${VERSION} ./processor

    push_and_deploy stonebank-application ${VERSION}
    push_and_deploy stonebank-consumer ${VERSION}
    push_and_deploy stonebank-processor ${VERSION}


    echo "${VERSION} Deployada!"
fi

