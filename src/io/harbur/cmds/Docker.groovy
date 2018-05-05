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

    tags = ""
    if (docker.tags) {
      echo "tags found"

      for (tag in docker.tags) {
        tags+= " -t ${docker.image}:${tag}"
      }
      sh "echo ${tags}"
    } else {
      echo "tags not found"
    }

    sh """
      docker build -t ${docker.image}:${docker.version} -f ${docker.path} ${docker.context}
    """
  }
}

def push() {
  def properties = new io.harbur.utils.Properties()

  for (docker in properties.project().docker) {
    sh """
      docker push ${docker.image}:${docker.version}
    """
  }
}