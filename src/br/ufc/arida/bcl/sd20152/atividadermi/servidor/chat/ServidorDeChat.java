package br.ufc.arida.bcl.sd20152.atividadermi.servidor.chat;

import br.ufc.arida.bcl.sd20152.atividadermi.servidor.chat.Chat;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import br.ufc.arida.bcl.sd20152.atividadermi.lib.InterfaceDeServidor;

public class ServidorDeChat {

    private Registry registro;

    private InterfaceDeServidor servidor;

    public ServidorDeChat() {

    }

    public void executar() {
        try {
            System.out.println(">Criando o chat");
            servidor = new Chat();
            System.out.println(">Chat criado");
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            registro = LocateRegistry.createRegistry(InterfaceDeServidor.PORTA);
            System.out.println(">registro criado na porta" + InterfaceDeServidor.PORTA);
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

        try {
            registro.rebind(InterfaceDeServidor.ID_DO_CHAT_RMI, servidor);
            System.out.println(">servidor no servico de nomes");
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public InterfaceDeServidor getServidor() {
        return servidor;
    }

}
