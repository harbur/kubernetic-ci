#!/usr/bin/groovy
package io.harbur.cmds

def checkout() {
  stage ('Checkout') {
    def scmVars = checkout scm
    def commitHash = scmVars.GIT_COMMIT
    println (commitHash)
  }
}