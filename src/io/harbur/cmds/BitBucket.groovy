#!/usr/bin/groovy
package io.harbur.cmds

def inProgress() {
  bitbucketStatusNotify(buildState: 'INPROGRESS', buildKey: 'build', buildName: 'Build')
}

def successful() {
  bitbucketStatusNotify( buildState: 'SUCCESSFUL', buildKey: 'build', buildName: 'Build')
}

def failed() {
  bitbucketStatusNotify(buildState: 'FAILED', buildKey: 'build', buildName: 'Build',
    buildDescription: 'Something went wrong with build!'
  )
}