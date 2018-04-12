#!/usr/bin/groovy
package io.harbur.utils

def global() {
  return readYaml file: '/pipeline/properties.yaml'
}

def project() {
  return readYaml file: 'kubernetic.yaml'
}