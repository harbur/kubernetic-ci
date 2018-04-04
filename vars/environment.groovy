def call(body) {
  node ("jenkins-jenkins-slave"){
    try{
      sh "docker login docker.k8s.harbur.io -u admin -p sQe3tokiLm"
        stage ('Checkout') {
          checkout scm
        }

        stage ('Deploy') {
          bitbucketStatusNotify(buildState: 'INPROGRESS', buildKey: 'deploy', buildName: 'Deploy')

        try {
          sh "helm init -c"
          sh "helm repo add chartmuseum http://chartmuseum-chartmuseum:8080"
          sh "helm dep build env/sandbox/"
          sh "helm upgrade -i sandbox env/sandbox/ --namespace kc-staging"

          bitbucketStatusNotify(buildState: 'SUCCESSFUL', buildKey: 'deploy', buildName: 'Deploy')
        } catch(Exception e) {
          bitbucketStatusNotify(buildState: 'FAILED', buildKey: 'deploy', buildName: 'Deploy',
            buildDescription: 'Something went wrong with deploy!'
          )
        }
      }
    } catch (e){
      throw e
    }
  }
}
