def call(body) {
  node ("jenkins-jenkins-slave"){
    try{
      sh "docker login docker.k8s.harbur.io -u admin -p sQe3tokiLm"
      docker.withRegistry('https://docker.k8s.harbur.io', 'REGISTRY') {
        kc_image = docker.image("docker.k8s.harbur.io/kc:55688e4")
        kc_image.inside('-v /home/jenkins/.docker/:/root/.docker/ -v /usr/bin/docker:/usr/bin/docker')  {
  
          stage ('Checkout') {
            checkout scm
          }
  
          stage ('Build') {
            bitbucketStatusNotify(buildState: 'INPROGRESS', buildKey: 'build', buildName: 'Build')

            try {
              sh '''
                helm init -c
                helm repo add incubator https://kubernetes-charts-incubator.storage.googleapis.com/
                for i in `ls -1 charts`; do
                  helm dep build "charts/$i"
                  helm package --destination docs "charts/$i"
                done
              '''

              bitbucketStatusNotify(buildState: 'SUCCESSFUL', buildKey: 'build', buildName: 'Build')
            } catch(Exception e) {
              bitbucketStatusNotify(buildState: 'FAILED', buildKey: 'build', buildName: 'Build',
                buildDescription: 'Something went wrong with build!'
              )
            }
          }
  
          stage ('Test') {
            bitbucketStatusNotify(buildState: 'INPROGRESS', buildKey: 'test', buildName: 'Test')

            try {
              sh "helm lint charts/*"
              bitbucketStatusNotify(buildState: 'SUCCESSFUL', buildKey: 'test', buildName: 'Test')
            } catch(Exception e) {
              bitbucketStatusNotify(buildState: 'FAILED', buildKey: 'test', buildName: 'Test',
                buildDescription: 'Something went wrong with build!'
              )
            }
          }

          stage ('Push') {
            bitbucketStatusNotify(buildState: 'INPROGRESS', buildKey: 'push', buildName: 'Push')
            try {
 def userInput = input(
 id: 'userInput', message: 'Let\'s promote?', parameters: [
 [$class: 'TextParameterDefinition', defaultValue: 'uat', description: 'Environment', name: 'env']
])
echo ("Env: "+userInput)

              sh '''
                for file in `ls -1 docs/*.tgz`; do
                  curl  -F "chart=@$file" https://chartmuseum.k8s.harbur.io/api/charts
                done
              '''

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
