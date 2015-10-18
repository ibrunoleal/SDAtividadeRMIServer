package br.ufc.arida.bcl.sd20152.atividadermi.servidor.chat;

import br.ufc.arida.bcl.sd20152.atividadermi.lib.InterfaceDeCliente;
import br.ufc.arida.bcl.sd20152.atividadermi.servidor.chat.Chat;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import br.ufc.arida.bcl.sd20152.atividadermi.lib.InterfaceDeServidor;
import br.ufc.arida.bcl.sd20152.atividadermi.lib.Mensagem;
import java.util.List;

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
                System.out.println(">registro ja existe na porta" + InterfaceDeServidor.PORTA + ". Foi recuperado.");
            } catch (RemoteException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                System.exit(0);
            }
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        registro.rebind(InterfaceDeServidor.ID_DO_CHAT_RMI, chat);
        System.out.println(">servidor no servico de nomes");
    }

    @Override
    public void adicionarCliente(InterfaceDeCliente cliente, String nickname) throws RemoteException {
        Usuario usuario = new Usuario(nickname, cliente);
        if (isUserInChat(usuario)) {
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public synchronized boolean isUserInChat(Usuario usuario) {
        for (Usuario user : getUsuariosDoChat()) {
            if (usuario.equals(user)) {
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

}
