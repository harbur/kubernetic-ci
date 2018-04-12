#!/usr/bin/groovy
package io.harbur

def init() {
  sh "helm init -c"
  def global_properties = readYaml file: '/pipeline/properties.yaml'
  for (repo in global_properties.repos) {
    sh "helm repo add ${repo.name} ${repo.url}"
  }
}

def pack() {
  def project_properties = readYaml file: 'kubernetic.yaml'
  for (chart in project_properties.charts) {
    sh """
      helm dep build "${chart}"
      helm package --destination docs "${chart}"
    """
  }
}

def test() {
  def project_properties = readYaml file: 'kubernetic.yaml'
  for (chart in project_properties.charts) {
    sh """
      helm lint "${chart}"
    """
  }
}

def push() {
  sh '''
    for file in `ls -1 docs/*.tgz`; do
      curl  -sF "chart=@$file" http://chartmuseum-chartmuseum:8080/api/charts
    done
  '''
}
