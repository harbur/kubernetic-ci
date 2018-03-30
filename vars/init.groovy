def call(body) {
  node ("jenkins-jenkins-slave"){
    try{
      stage ('Build') {
        checkout scm
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
