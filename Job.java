import java.io.PrintWriter;

public class Job {

  PrintWriter output;

  String cmd = "";
  Double firstNum = null;
  Double secNum = null;

  public Job(String command, PrintWriter output) {
    this.output = output;

    String[] commandParts = command.split(",");

    if (commandParts.length == 3) {
      cmd = commandParts[0];

      try {
        firstNum = Double.parseDouble(commandParts[1]);
        secNum = Double.parseDouble(commandParts[2]);
      } catch (Exception e) {
        cmd = "";
      }
    }

  }

  public void doJob() {

    String answer = "";

    switch (cmd) {
      case "ADD":
        answer += firstNum + secNum;
        break;

      case "SUB":
        answer += firstNum - secNum;
        break;

      case "MUL":
        answer += firstNum * secNum;
        break;

      case "DIV":
        answer += firstNum / secNum;
        break;

      default:
        answer = "Invalid command entered";
    }

    output.println(answer);
  }
}