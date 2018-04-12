#!/usr/bin/groovy
package io.harbur.cmds

def checkout() {
  stage ('Checkout') {
    checkout scm
  }
}