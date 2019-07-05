#!/usr/bin/groovy
package io.harbur.cmds

def login() {
    def properties = new io.harbur.utils.Properties()
    def registry = properties.global().registry

    echo "Authenticating to Registry ${registry}"

    withCredentials([[
      $class: 'UsernamePasswordMultiBinding',
      credentialsId: 'REGISTRY',
      usernameVariable: 'DOCKER_USERNAME',
      passwordVariable: 'DOCKER_PASSWORD'
    ]]) {
      sh "docker login ${registry} -u ${DOCKER_USERNAME} -p ${DOCKER_PASSWORD}"
    }

    withCredentials([[
      $class: 'UsernamePasswordMultiBinding',
      credentialsId: 'REGISTRY',
      usernameVariable: 'DOCKER_USERNAME',
      passwordVariable: 'DOCKER_PASSWORD'
    ]]) {
      sh "docker login ${registry} -u ${DOCKER_USERNAME} -p ${DOCKER_PASSWORD}"
    }
}

def build() {
  def properties = new io.harbur.utils.Properties()
  def registry = properties.global().registry

  for (config in properties.project().docker) {

    // tag_params = ""
    // if (docker.tags) {
    //   for (tag in docker.tags) {
    //     tag = sh(script: "echo -n ${tag}", returnStdout: true).replaceAll('/','.')
    //     tag_params+= " -t ${docker.image}:${tag}"
    //   }
    // }
    echo "Authenticating to Registry ${registry}"
    docker.withRegistry("https://" + registry, 'REGISTRY') {
      def customImage = docker.build("${config.image}:${env.BUILD_ID}", "-f ${config.path} ${config.context}")
      customImage.push()
    }
  }
}

def push() {
  def properties = new io.harbur.utils.Properties()

  for (docker in properties.project().docker) {
    for (tag in docker.tags) {
      tag = sh(script: "echo -n ${tag}", returnStdout: true).replaceAll('/','.')
      sh """
        docker push ${docker.image}:${tag}
      """
    }
  }
}