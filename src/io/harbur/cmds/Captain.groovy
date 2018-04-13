#!/usr/bin/groovy
package io.harbur.cmds

def build() {
  sh "captain build -t ${BRANCH_NAME}"
}

def test() {
  sh "captain test -t ${BRANCH_NAME}"
}

def push() {
  sh "captain push -t ${BRANCH_NAME}"
}