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

  print "ENV: ${env}"
  sh """
  env
  """

  for (docker in properties.project().docker) {
    sh """
    echo docker build -t ${docker.image}
    """
    FOO = sh (script: "echo docker build -t ${docker.image}", returnStdout: true)
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