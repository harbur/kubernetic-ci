#!/usr/bin/groovy
package io.harbur.utils

def global() {
  def global_properties = readYaml file: '/pipeline/properties.yaml'
  return global_properties
}

def project() {
  def project_properties = readYaml file: 'kubernetic.yaml'
  return project_properties
}