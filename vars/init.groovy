def call(body) {
  node ("jenkins-jenkins-slave"){
    try{
      sh "docker login docker.k8s.harbur.io -u admin -p admin123"
      docker.withRegistry('https://docker.k8s.harbur.io', 'REGISTRY') {
        kc_image = docker.image("docker.k8s.harbur.io/kc:04d0fc5")
        kc_image.inside('-v /home/jenkins/.docker/:/root/.docker/ -v /usr/bin/docker:/usr/bin/docker')  {
          stage ('Checkout') {
            checkout scm
          }

          bitbucketStatusNotify(buildState: 'INPROGRESS', buildKey: 'build', buildName: 'Build')
          try {
            stage ('Build') {
              sh "kc build -t ${BRANCH_NAME}"
            }
            bitbucketStatusNotify(buildState: 'SUCCESSFUL', buildKey: 'build', buildName: 'Build')
          } catch(Exception e) {
            bitbucketStatusNotify(buildState: 'FAILED', buildKey: 'build', buildName: 'Build',
              buildDescription: 'Something went wrong with build!'
            )
          }
  
          stage ('Push') {
            bitbucketStatusNotify(buildState: 'INPROGRESS', buildKey: 'push', buildName: 'Push')
            try {
              sh "kc push -t ${BRANCH_NAME}"
              bitbucketStatusNotify( buildState: 'SUCCESSFUL', buildKey: 'push', buildName: 'Push')
            } catch(Exception e) {
              bitbucketStatusNotify(buildState: 'FAILED', buildKey: 'push', buildName: 'Push',
                buildDescription: 'Something went wrong while pushing image(s) to the registry!'
              )
            }

          }
        }
      }
    } catch (e){
      throw e
    }
  }
}
