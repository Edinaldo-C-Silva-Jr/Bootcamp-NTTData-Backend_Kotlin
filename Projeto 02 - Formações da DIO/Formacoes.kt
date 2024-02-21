enum class Nivel {
    Basico,
    Intermediario,
    Avancado
}

data class Aluno(val nome: String) {
    override fun toString() = nome
}

data class ConteudoEducacional(val nome: String, val duracao: Int = 60) {
    override fun toString() = "$nome ($duracao min)"
}

data class Formacao(val nome: String, val conteudos: List<ConteudoEducacional>) {    
    val inscritos = mutableListOf<Aluno>()
    val alunos: List<Aluno> = inscritos
    
    fun matricular(vararg alunos: Aluno) {
        for (aluno in alunos) {
            inscritos.add(aluno)
        }
    }
    
    fun verificarAlunos() = alunos
    
    fun verificarConteudos() = conteudos
}

fun main() {
    val aluno1 = Aluno("João Silva")
    val aluno2 = Aluno("Maria Santos")
    val aluno3 = Aluno("José Souza")
    
    val conteudosDoCurso = listOf(
        ConteudoEducacional("Controle de Fluxo", 120),
        ConteudoEducacional("Coleções"),
        ConteudoEducacional("Orientação a Objetos", 180),
        ConteudoEducacional("Funções", 120),
        ConteudoEducacional("Exceções"),
    )
    
    val formacaoKotlin = Formacao("Formação Kotlin", conteudosDoCurso)
    
    formacaoKotlin.matricular(aluno1, aluno2, aluno3)
    
    with (formacaoKotlin) {
        println("Alunos inscritos em $nome: ${verificarAlunos()}")
        println("Conteudos disponíveis em $nome: ${verificarConteudos()}")
    }
}