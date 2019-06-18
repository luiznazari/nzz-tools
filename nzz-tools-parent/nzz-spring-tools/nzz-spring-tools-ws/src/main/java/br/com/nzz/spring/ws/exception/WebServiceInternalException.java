package br.com.nzz.spring.ws.exception;

/**
 * Exceção lançada pelos serviços que gerenciam processos relacionados aos WebServices.
 * 
 * @author Luiz Felipe Nazari
 */
public class WebServiceInternalException extends Exception {

    private static final long serialVersionUID = 4735186022368404957L;

    public WebServiceInternalException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public WebServiceInternalRuntimeException runtime() {
        return new WebServiceInternalRuntimeException(this.getMessage(), this.getCause());
    }

    /**
     * Instância de exceção dos WebServices em tempo de execução.
     * 
     * @author Luiz Felipe Nazari
     */
    private static class WebServiceInternalRuntimeException extends RuntimeException {

        private static final long serialVersionUID = -4556770168592273083L;

        private WebServiceInternalRuntimeException(String message, Throwable throwable) {
            super(message, throwable);
        }

    }

}
