def call(body) {
  node ("jenkins-jenkins-slave"){
    try{
      stage ('Build') {
        checkout scm
        checkout scm
        //git branch: 'master', credentialsId: '12345-1234-4696-af25-123455', url: 'ssh://git@bitbucket.org:company/repo.git'
        withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'KUBERNETIC_ACCESS_KEY', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
          sh "echo $USERNAME - $PASSWORD ::::"
        }

        sh "docker build -t demo ."
        echo "hello"
      }
      stage ('Push') {
        echo "done"
      }
    } catch (e){
      throw e
    }
  }
}
