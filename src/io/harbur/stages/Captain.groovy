#!/usr/bin/groovy
package io.harbur.stages

def job() {
  def properties = new io.harbur.utils.Properties()
  if (properties.project().hasProperty("captain") {
    build()
    test()
    push()
  }
}

def build() {
  def captain = new io.harbur.cmds.Captain()

  stage ('Captain Build') {
    captain.build()
  }
}

def test() {
  def captain = new io.harbur.cmds.Captain()

  stage ('Captain Test') {
    captain.test()
  }
}

def push() {
  def captain = new io.harbur.cmds.Captain()

  stage ('Captain Push') {
    captain.push()
  }
}
