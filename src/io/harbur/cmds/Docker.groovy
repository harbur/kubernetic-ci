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
}

def build() {
  def properties = new io.harbur.utils.Properties()

  for (docker in properties.project().docker) {

    tag_params = ""
    if (docker.tags) {
      for (tag in docker.tags) {
        tag_params+= " -t ${docker.image}:${tag}"
      }
    }

    sh """
      docker build ${tag_params} -f ${docker.path} ${docker.context}
    """
  }
}

def push() {
  def properties = new io.harbur.utils.Properties()

  for (docker in properties.project().docker) {
    for (tag in docker.tags) {
      sh """
        docker push ${docker.image}:${tag}
      """
    }
  }
}