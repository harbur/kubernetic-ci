#!/usr/bin/groovy
package io.harbur.stages

def git = new io.harbur.cmds.Git()

def checkout() {
  stage ('Checkout') {
    git.checkout()
  }
}

def build() {
}

def test() {
}

def push() {
}

def deploy() {
}
