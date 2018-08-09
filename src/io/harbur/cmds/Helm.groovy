#!/usr/bin/groovy
package io.harbur.cmds

def init() {
  sh "helm init -c"
}

def addRepos() {
  def properties = new io.harbur.utils.Properties()

  for (repo in properties.global().repos) {
    if (repo.username && repo.password) {
      sh "helm repo add ${repo.name} --username ${repo.username} --password ${repo.password} ${repo.url}"
    } else {
      sh "helm repo add ${repo.name} ${repo.url}"
    }
  }
}

def pack() {
  def properties = new io.harbur.utils.Properties()

  for (chart in properties.project().charts) {
    sh """
      mkdir -p build/packages
      helm dep build "${chart}"
      helm package --destination build/packages/ "${chart}"
    """
  }
}

def upgrade() {
  def properties = new io.harbur.utils.Properties()

  for (environment in properties.project().environments) {
    sh """
    helm dep build ${environment.path}
    helm upgrade -i ${environment.name} ${environment.path} --namespace ${environment.namespace}
    """
  }
}

def test() {
  def properties = new io.harbur.utils.Properties()

  for (chart in properties.project().charts) {
    sh """
      helm lint "${chart}"
    """
  }
}

def push() {
  def properties = new io.harbur.utils.Properties()
  def chartRepo = properties.global().chartRepo

  sh """
    for file in `ls -1 build/packages/*.tgz`; do
      curl  -sF "chart=@\$file" http://${chartRepo}:8080/api/charts
    done
  """
}
