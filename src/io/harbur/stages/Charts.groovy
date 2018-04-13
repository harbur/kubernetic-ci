#!/usr/bin/groovy
package io.harbur.stages

def job() {
  def properties = new io.harbur.utils.Properties()
  if (properties.project().charts) {
    build()
    test()
    push()
  }
}

def build() {
  def helm = new io.harbur.cmds.Helm()

  stage ('Charts Build') {
    helm.init()
    helm.addRepos()
    helm.pack()
  }
}

def test() {
  def helm = new io.harbur.cmds.Helm()

  stage ('Charts Test') {
    helm.test()
  }
}

def push() {
  def helm = new io.harbur.cmds.Helm()

  stage ('Charts Push') {
    helm.push()
  }
}

def deploy() {
}
