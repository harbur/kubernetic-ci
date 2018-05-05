#!/usr/bin/groovy
package io.harbur.cmds

def checkout() {
  stage ('Checkout') {
    def scmVars = checkout scm
    def commitHash = scmVars.GIT_COMMIT

    // Make SCM variables available in environment
    env.GIT_COMMIT=scmVars.GIT_COMMIT
    env.GIT_BRANCH=scmVars.GIT_BRANCH
    env.GIT_PREVIOUS_COMMIT=scmVars.GIT_PREVIOUS_COMMIT
    env.GIT_PREVIOUS_SUCCESSFUL_COMMIT=scmVars.GIT_PREVIOUS_SUCCESSFUL_COMMIT
    env.GIT_URL=scmVars.GIT_URL
  }
}