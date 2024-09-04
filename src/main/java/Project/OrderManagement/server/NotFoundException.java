package Project.OrderManagement.server;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String message){
        super(message);
    }
}
