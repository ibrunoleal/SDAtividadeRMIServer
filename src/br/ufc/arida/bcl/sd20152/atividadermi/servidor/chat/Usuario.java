package br.ufc.arida.bcl.sd20152.atividadermi.servidor.chat;

import br.ufc.arida.bcl.sd20152.atividadermi.lib.InterfaceDeCliente;

public class Usuario {

	private String nickname;
	
	private InterfaceDeCliente cliente;

	public Usuario(String nickname, InterfaceDeCliente cliente) {
		this.nickname = nickname;
		this.cliente = cliente;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public InterfaceDeCliente getCliente() {
		return cliente;
	}

	public void setCliente(InterfaceDeCliente cliente) {
		this.cliente = cliente;
	}

	@Override
	public String toString() {
		return "Usuario [nickname=" + nickname + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((nickname == null) ? 0 : nickname.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (nickname == null) {
			if (other.nickname != null)
				return false;
		} else if (!nickname.equals(other.nickname))
			return false;
		return true;
	}
	
	
	
}
