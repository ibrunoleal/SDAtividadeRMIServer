package br.ufc.arida.bcl.sd20152.atividadermi.servidor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import br.ufc.arida.bcl.sd20152.atividadermi.lib.InterfaceDeCliente;
import br.ufc.arida.bcl.sd20152.atividadermi.lib.InterfaceDeServidor;
import br.ufc.arida.bcl.sd20152.atividadermi.lib.Mensagem;

@SuppressWarnings("serial")
public class Chat extends UnicastRemoteObject implements InterfaceDeServidor {

    private int contador;

    private List<Usuario> usuarios;

    private List<String> mensagensDeLog;

    protected Chat() throws RemoteException {
        this.contador = 0;
        usuarios = new ArrayList<Usuario>();
        mensagensDeLog = new ArrayList<String>();
    }

    @Override
    public synchronized void adicionarCliente(InterfaceDeCliente cliente, String nickname) throws RemoteException {
        Usuario usuario = new Usuario(nickname, cliente);
        if (isUserInClientes(usuario)) {
            String textoDeLog = ">Usuario nao pode ser adicionado. Nickname existente: " + nickname;
            mensagensDeLog.add(textoDeLog);
            System.out.println(textoDeLog);
        } else {
            usuarios.add(usuario);
            String textoDeLog = ">Usuario conectado ao servidor: " + nickname;
            mensagensDeLog.add(textoDeLog);
            System.out.println(textoDeLog);
        }

    }

    @Override
    public synchronized void removerCliente(InterfaceDeCliente cliente) throws RemoteException {
        for (Usuario usuario : usuarios) {
            if (usuario.getCliente().equals(cliente)) {
                usuarios.remove(usuario);
                String textoDeLog = ">Usuario removido dos assinantes com sucesso: " + usuario.getNickname();
                mensagensDeLog.add(textoDeLog);
                System.out.println(textoDeLog);
                break;
            }
        }
    }

    @Override
    public synchronized void enviarMensagem(Mensagem mensagem) throws RemoteException {
        mensagem.setId(contador);
        contador++;
        enviarMensagemParaOsClientes(mensagem);
    }

    public synchronized void enviarMensagemParaOsClientes(Mensagem mensagem) {
        for (Usuario usuario : usuarios) {
            try {
                usuario.getCliente().receberMensagem(mensagem);
                String textoDeLog = ">Mensagem enviada para o clietne " + usuario.getNickname() + "->" + mensagem;
                mensagensDeLog.add(textoDeLog);
                System.out.println(textoDeLog);
            } catch (RemoteException e) {
                String textoDeLog = ">Não foi possível enviar mensagem para o cliente" + usuario.getNickname() + "->" + mensagem;
                mensagensDeLog.add(textoDeLog);
                System.out.println(textoDeLog);
                try {
                    removerCliente(usuario.getCliente());
                } catch (RemoteException e1) {
                    System.out.println();
                    String textoDeLog2 = ">Erro ao remover cliente" + usuario.getNickname();
                    mensagensDeLog.add(textoDeLog2);
                    System.out.println(textoDeLog2);
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
        }
    }

    public boolean isUserInClientes(Usuario usuario) {
        for (Usuario user : usuarios) {
            if (usuario.equals(user)) {
                return true;
            }
        }
        return false;
    }

    public synchronized List<String> getMensagensDeLog() {
        return mensagensDeLog;
    }
    
    

}
