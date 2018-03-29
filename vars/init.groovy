def call(body) {
  node ("master"){
    try{
      stage 'Init'
      echo "hello"
      stage 'Build'
      echo "done"
    } catch (e){
      throw e
    }
  }
}
