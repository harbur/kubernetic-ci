#!/usr/bin/groovy
package io.harbur

def checkout() {
  stage ('Checkout') {
    checkout scm
  }
}