#!/usr/bin/groovy
package io.harbur

def init() {
  sh "helm init -c"
  def properties = readYaml file: '/pipeline/properties.yaml'
  for (repo in properties.repos) {
    sh "helm repo add ${repo.name} ${repo.url}"
  }
}