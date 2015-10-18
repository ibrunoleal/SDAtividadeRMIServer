package br.ufc.arida.bcl.sd20152.atividadermi.servidor.chat;

import br.ufc.arida.bcl.sd20152.atividadermi.lib.InterfaceDeCliente;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import br.ufc.arida.bcl.sd20152.atividadermi.lib.Mensagem;

public class Chat {
    
    public static final String nickDoServidor = "@Servidor";

    private int contadorDeMensagens;

    private List<Usuario> usuarios;

    private List<String> mensagensDeLog;

    protected Chat() {
        this.contadorDeMensagens = 0;
        usuarios = new ArrayList<Usuario>();
        mensagensDeLog = new ArrayList<String>();
    }

    public boolean adicionarUsuario(Usuario usuario) {
        String log;
        if (this.usuarios.add(usuario)) {
            log = "Usuario adicionado ao chat com sucesso: " + usuario.getNickname();
            adicionarRegistroDeLog(log);
            Mensagem m = new Mensagem("@Servidor", "[" + usuario.getNickname() + " entrou no chat]");
            enviarMensagem(m);
            return true;
        }
        log = "Usuario NAO foi adicionado ao chat: " + usuario.getNickname();
        return false;
    }

    public boolean removerUsuario(Usuario usuario) {
        String log;
        if (this.usuarios.remove(usuario)) {
            log = "Usuario removido do chat com sucesso: " + usuario.getNickname();
            adicionarRegistroDeLog(log);
            Mensagem m = new Mensagem("@Servidor", "[" + usuario.getNickname() + " saiu do chat]");
            enviarMensagem(m);
            return true;
        }
        log = "Usuario NAO foi removido do chat: " + usuario.getNickname();
        return false;
    }
    
    public boolean removerUsuario(InterfaceDeCliente cliente) {
        String log;
        Usuario usuario = getUsuarioDoChat(cliente);
        if (usuario != null) {
            usuarios.remove(usuario);
            log = "Usuario removido do chat com sucesso: " + usuario.getNickname();
            adicionarRegistroDeLog(log);
            Mensagem m = new Mensagem("@Servidor", "[" + usuario.getNickname() + " saiu do chat]");
            enviarMensagem(m);
            return true;
        }
        log = "Usuario NAO foi removido do chat: usuario nao localizado";
        adicionarRegistroDeLog(log);
        return false;
    }

    public void enviarMensagem(Mensagem mensagem) {
        mensagem.setId(contadorDeMensagens);
        contadorDeMensagens++;
        enviarMensagemParaOsClientes(mensagem);
    }

    public synchronized void enviarMensagemParaOsClientes(Mensagem mensagem) {
        for (Usuario usuario : usuarios) {
            try {
                usuario.getCliente().receberMensagem(mensagem);
                String textoDeLog = "Mensagem enviada para o clietne " + usuario.getNickname() + "->" + mensagem;
                adicionarRegistroDeLog(textoDeLog);
            } catch (RemoteException e) {
                String textoDeLog = "Não foi possível enviar mensagem para o cliente" + usuario.getNickname() + "->" + mensagem;
                adicionarRegistroDeLog(textoDeLog);
                removerUsuario(usuario);
                e.printStackTrace();
            }
        }
    }

    public synchronized boolean isUserInClientes(Usuario usuario) {
        for (Usuario user : usuarios) {
            if (usuario.equals(user)) {
                return true;
            }
        }
        return false;
    }

    public synchronized List<Usuario> getUsuarios() {
        List<Usuario> listaDeUsuarios = new ArrayList<Usuario>(usuarios);
        return listaDeUsuarios;
    }

    public synchronized List<String> getMensagensDeLog() {
        List<String> listaDeMensagensDeLog = new ArrayList<String>(mensagensDeLog);
        return listaDeMensagensDeLog;
    }
    
    public void adicionarRegistroDeLog(String registroDeLog) {
        String log = ">" + registroDeLog;
        mensagensDeLog.add(log);
        System.out.println(log);
    }
    
    public Usuario getUsuarioDoChat(InterfaceDeCliente cliente) {
        for (Usuario usuario : getUsuarios()) {
            if (usuario.getCliente().equals(cliente)) {
                return usuario;
            }
        }
        return null;
    }

}
