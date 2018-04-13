#!/usr/bin/groovy
package io.harbur.stages

def checkout() {
  def git = new io.harbur.cmds.Git()

  stage ('Checkout') {
    git.checkout()
  }
}

def build() {
  def charts = new io.harbur.stages.Charts()

  charts.job()
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
