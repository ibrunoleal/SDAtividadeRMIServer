package br.ufc.arida.bcl.sd20152.atividadermi.servidor.chat;

import br.ufc.arida.bcl.sd20152.atividadermi.lib.InterfaceDeCliente;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import br.ufc.arida.bcl.sd20152.atividadermi.lib.InterfaceDeServidor;
import br.ufc.arida.bcl.sd20152.atividadermi.lib.Mensagem;
import java.rmi.Remote;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatController extends UnicastRemoteObject implements InterfaceDeServidor, Remote {

    private Registry registro;

    private Chat chat;
    
    //private final String IP_SERVIDOR = "localhost";
    private final int PORTA = 1099;
    private final String ID_DO_SERVICO = "chatrmi";
    //private final String registroDeRMI = "rmi://" + IP_SERVIDOR + ":" + PORTA + "/" + ID_DO_SERVICO;

    public ChatController() throws RemoteException{
        chat = new Chat();
    }

    public void executar() {
        String log;

        log = "Criando o chat...";
        chat.adicionarRegistroDeLog(log);
        chat = new Chat();
        log = "Chat criado";
        chat.adicionarRegistroDeLog(log);

        try {
            registro = LocateRegistry.createRegistry(PORTA);
            log = "registro criado na porta " + PORTA;
            adicionarRegistroDeLog(log);
        } catch (RemoteException e) {
            try {
                registro = LocateRegistry.getRegistry(PORTA);
                log = "registro ja existe na porta " + PORTA + ". Foi recuperado.";
                adicionarRegistroDeLog(log);
            } catch (RemoteException e1) {
                log = "nao foi possivel criar registro do chat na porta " + PORTA + ".";
                adicionarRegistroDeLog(log);
                e1.printStackTrace();
                //System.exit(0);
            }
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            registro.rebind(ID_DO_SERVICO, this);
            log = "servidor registrado no servico de nomes com id: " + ID_DO_SERVICO;
            adicionarRegistroDeLog(log);
        } catch (RemoteException ex) {
            log = "nao foi possivel registrar o servidor no servico de nomes com id: " + ID_DO_SERVICO;
            adicionarRegistroDeLog(log);
            Logger.getLogger(ChatController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public boolean adicionarCliente(InterfaceDeCliente cliente, String nickname) throws RemoteException {
        Usuario usuario = new Usuario(nickname, cliente);
        if (isUsuarioNoChat(usuario) || nickname.equalsIgnoreCase(Chat.nickDoServidor)) {
            String textoDeLog = "Usuario nao pode ser adicionado. Nickname existente: " + nickname;
            adicionarRegistroDeLog(textoDeLog);
            return false;
        } else {
            return chat.adicionarUsuario(usuario);
        }
    }

    @Override
    public boolean removerCliente(InterfaceDeCliente cliente) throws RemoteException {
       return chat.removerUsuario(cliente);
    }

    @Override
    public void enviarMensagem(Mensagem mensagem) throws RemoteException {
        chat.enviarMensagem(mensagem);
    }
    
    @Override
    public List<String> getNicknamesDosUsuarios() throws RemoteException {
        List<String> listaDeNicknameDosUsuarios = new ArrayList<String>();
        for (Usuario usuario : getUsuariosDoChat()) {
            String nick = usuario.getNickname();
            listaDeNicknameDosUsuarios.add(nick);
        }
        return listaDeNicknameDosUsuarios;
    }
    
    public boolean isUsuarioNoChat(Usuario usuario) {
        for (Usuario user : getUsuariosDoChat()) {
            if (usuario.equals(user)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isUsuarioNoChat(String nickname) {
        for (Usuario user : getUsuariosDoChat()) {
            if (user.getNickname().equalsIgnoreCase(nickname)) {
                return true;
            }
        }
        return false;
    }
        
    public List<Usuario> getUsuariosDoChat() {
        return chat.getUsuarios();
    }
    
    public void adicionarRegistroDeLog(String registroDeLog) {
        chat.adicionarRegistroDeLog(registroDeLog);
    }

    public List<String> getMensagensDeLog() {
        return chat.getMensagensDeLog();
    }
    
}
