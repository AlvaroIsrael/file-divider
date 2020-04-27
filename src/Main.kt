import mu.KLogging
import java.io.BufferedWriter
import java.io.FileWriter
import java.util.*
import javax.swing.JFileChooser
import javax.swing.JOptionPane
import javax.swing.filechooser.FileNameExtensionFilter
import kotlin.system.exitProcess

class Main {
  companion object Logger : KLogging() {
    @JvmStatic
    fun main(args: Array<String>) {

      val kTamanhoMaximoEmMegaBytes = 19.0
      val seletorArquivos = JFileChooser()
      seletorArquivos.currentDirectory = java.io.File(".")
      seletorArquivos.dialogTitle = "Selecione o arquivo para dividir"
      seletorArquivos.fileSelectionMode = JFileChooser.FILES_AND_DIRECTORIES
      seletorArquivos.addChoosableFileFilter(FileNameExtensionFilter("*.txt", "txt"))
      seletorArquivos.fileFilter = FileNameExtensionFilter("*.txt", "txt")

      if (seletorArquivos.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
        exitProcess(1)
      }

      val arquivoOrigem = Scanner(seletorArquivos.selectedFile)
      var arquivoDestino = BufferedWriter(FileWriter(seletorArquivos.currentDirectory.toString() + "\\saida_" + Date().time + ".txt", true))
      var arquivoTamanho = 0

      try {
        while (arquivoOrigem.hasNextLine()) {
          val linha = arquivoOrigem.nextLine()
          if (arquivoTamanho + linha.toByteArray().size > kTamanhoMaximoEmMegaBytes * 1024.0 * 1024.0) {
            arquivoDestino.flush()
            arquivoDestino.close()
            arquivoDestino = BufferedWriter(FileWriter(seletorArquivos.currentDirectory.toString() + "\\saida_" + Date().time + ".txt", true))
            arquivoDestino.write(linha + "\n")
            arquivoTamanho = linha.toByteArray().size
          } else {
            arquivoDestino.write(linha + "\n")
            arquivoTamanho += linha.toByteArray().size
          }
        }
        arquivoDestino.flush()
        arquivoDestino.close()
        arquivoOrigem.close()
        JOptionPane.showMessageDialog(seletorArquivos,
          "Operacao completada com sucesso!",
          "Sucesso!",
          JOptionPane.INFORMATION_MESSAGE)
      } catch (erro: Exception) {
        logger.error { "Falha ao ler o arquivo: $erro." }
        arquivoDestino.flush()
        arquivoDestino.close()
        arquivoOrigem.close()
        JOptionPane.showMessageDialog(seletorArquivos,
          "Operacao completada com erro!",
          "Erro!",
          JOptionPane.ERROR_MESSAGE)
      }
    }
  }
}
