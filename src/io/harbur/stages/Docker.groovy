#!/usr/bin/groovy
package io.harbur.stages

def job() {
  def properties = new io.harbur.utils.Properties()
  if (properties.project().docker) {
    build()
  }
}

def build() {
  def docker = new io.harbur.cmds.Docker()

  stage ('Docker Build') {
    docker.build()
  }
}

def push() {
  def docker = new io.harbur.cmds.Docker()

  stage ('Docker Push') {
    docker.push()
  }
}
