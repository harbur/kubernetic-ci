#!/usr/bin/groovy
package io.harbur.stages

def checkout() {
  def git = new io.harbur.cmds.Git()

  stage ('Checkout') {
    git.checkout()
  }
}

def build() {
  def helm = new io.harbur.cmds.Helm()

  stage ('Build') {
    helm.init()
    helm.addRepos()
    helm.pack()
  }
}

def test() {
  def helm = new io.harbur.cmds.Helm()

  stage ('Test') {
    helm.test()
  }
}

def push() {
  def helm = new io.harbur.cmds.Helm()

  stage ('Push') {
    helm.push()
  }
}

def deploy() {
}
