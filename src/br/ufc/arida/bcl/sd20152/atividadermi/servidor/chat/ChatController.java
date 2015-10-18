package br.ufc.arida.bcl.sd20152.atividadermi.servidor.chat;

import br.ufc.arida.bcl.sd20152.atividadermi.lib.InterfaceDeCliente;
import br.ufc.arida.bcl.sd20152.atividadermi.servidor.chat.Chat;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import br.ufc.arida.bcl.sd20152.atividadermi.lib.InterfaceDeServidor;
import br.ufc.arida.bcl.sd20152.atividadermi.lib.Mensagem;
import java.rmi.Remote;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatController implements InterfaceDeServidor {

    private Registry registro;

    private Chat chat;

    public ChatController() {

    }

    public void executar() {
        String log;

        log = "Criando o chat...";
        chat.adicionarRegistroDeLog(log);
        chat = new Chat();
        log = "Chat criado";
        chat.adicionarRegistroDeLog(log);


        try {
            registro = LocateRegistry.createRegistry(InterfaceDeServidor.PORTA);
            log = "registro criado na porta " + InterfaceDeServidor.PORTA;
            adicionarRegistroDeLog(log);
        } catch (RemoteException e) {
            try {
                registro = LocateRegistry.getRegistry(InterfaceDeServidor.PORTA);
                log = "registro ja existe na porta " + InterfaceDeServidor.PORTA + ". Foi recuperado.";
                adicionarRegistroDeLog(log);
            } catch (RemoteException e1) {
                log = "nao foi possivel criar registro do chat na porta " + InterfaceDeServidor.PORTA + ".";
                adicionarRegistroDeLog(log);
                e1.printStackTrace();
                //System.exit(0);
            }
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            registro.rebind(InterfaceDeServidor.ID_DO_CHAT_RMI, (InterfaceDeServidor)chat);
            log = "servidor registrado no servico de nomes com id: " + InterfaceDeServidor.ID_DO_CHAT_RMI;
            adicionarRegistroDeLog(log);
        } catch (RemoteException ex) {
            log = "nao foi possivel registrar o servidor no servico de nomes com id: " + InterfaceDeServidor.ID_DO_CHAT_RMI;
            adicionarRegistroDeLog(log);
            Logger.getLogger(ChatController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void adicionarCliente(InterfaceDeCliente cliente, String nickname) throws RemoteException {
        Usuario usuario = new Usuario(nickname, cliente);
        if (isUsuarioNoChat(usuario)) {
            String textoDeLog = "Usuario nao pode ser adicionado. Nickname existente: " + nickname;
            adicionarRegistroDeLog(textoDeLog);
        } else {
            chat.adicionarUsuario(usuario);
        }
    }

    @Override
    public void removerCliente(InterfaceDeCliente cliente) throws RemoteException {
       chat.removerUsuario(cliente);
    }

    @Override
    public void enviarMensagem(Mensagem mensagem) throws RemoteException {
        chat.enviarMensagem(mensagem);
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
