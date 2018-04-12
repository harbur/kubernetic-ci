#!/usr/bin/groovy
package io.harbur.cmds

def build() {
  sh "kc build -t ${BRANCH_NAME}"
}

def push() {
  sh "kc push -t ${BRANCH_NAME}"
}