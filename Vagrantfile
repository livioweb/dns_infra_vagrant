
# -*- mode: ruby -*-
# vi: set ft=ruby :
VAGRANTFILE_API_VERSION = "2"

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|

  config.ssh.insert_key = true
  #config.disksize.size = '20GB'

    config.vm.provision "shell", inline: <<-SHELL
        export DEBIAN_FRONTEND=noninteractive
        apt-get update
        sudo apt-get install -y bind9 bind9utils bind9-doc
        #sudo groupadd endor
        #sudo usermod -aG bind vagrant
        #sudo usermod -aG bind ${USER}
    SHELL

  config.vm.define "endor1" do |endor1|
      endor1.vm.box = "ubuntu/bionic64"
      endor1.vm.network "private_network", ip: "172.17.177.53"
      #endor1.disksize.size = '2GB'
      endor1.vm.hostname = "endor1"
      endor1.vm.synced_folder "endor1/", "/home/vagrant/endor1"
      endor1.vm.provision "shell", path: "endor1/script.sh"


        endor1.vm.provision "shell", inline: <<-SHELL
            #rm /etc/bind/named.conf.options
            #rm /etc/bind/named.conf.local
            #rm /etc/default/bind9
            #rm -r /etc/bind/zones
            #            rm /etc/netplan/00-private-nameservers.yaml
        SHELL


        endor1.vm.provision "file", source: "endor1/bind9", destination: "/tmp/bind9"
        endor1.vm.provision "shell", inline: "mv /tmp/bind9 /etc/default/bind9"

        endor1.vm.provision "shell", inline: "sudo systemctl restart bind9"

        endor1.vm.provision "file", source: "endor1/named.conf.options", destination: "/tmp/named.conf.options"
        endor1.vm.provision "shell", inline: "mv /tmp/named.conf.options /etc/bind/named.conf.options"

        endor1.vm.provision "file", source: "endor1/named.conf.local", destination: "/tmp/named.conf.local"
        endor1.vm.provision "shell", inline: "mv /tmp/named.conf.local /etc/bind/named.conf.local"

        endor1.vm.provision "shell", inline: "sudo mkdir /etc/bind/zones"

        endor1.vm.provision "file", source: "endor1/db.devops", destination: "/tmp/db.devops"
        endor1.vm.provision "shell", inline: "mv /tmp/db.devops /etc/bind/zones/db.devops"

        endor1.vm.provision "file", source: "endor1/db.172.17", destination: "/tmp/db.172.17"
        endor1.vm.provision "shell", inline: "mv /tmp/db.172.17 /etc/bind/zones/db.172.17"

        endor1.vm.provision "file", source: "endor1/00-private-nameservers.yaml", destination: "/tmp/00-private-nameservers.yaml"
        endor1.vm.provision "shell", inline: "mv /tmp/00-private-nameservers.yaml /etc/netplan/00-private-nameservers.yaml"


          endor1.vm.provider :virtualbox do |endor1|
              endor1.customize ["modifyvm", :id, "--memory", "1024"]
              endor1.customize ["modifyvm", :id, "--cpus", "1"]
          end
    end


    config.vm.define "endor2" do |endor2|
      endor2.vm.box = "ubuntu/bionic64"
      endor2.vm.network "private_network", ip: "172.17.177.54"
      #endor2.disksize.size = '2GB'
      endor2.vm.hostname = "endor2"
      endor2.vm.synced_folder "endor2/", "/home/vagrant/endor2"
      endor2.vm.provision "shell", path: "endor2/script.sh"


      endor2.vm.provision "shell", inline: <<-SHELL
            #rm /etc/bind/named.conf.options
            #rm /etc/bind/named.conf.local
            #rm /etc/default/bind9
            #rm /etc/netplan/00-private-nameservers.yaml
        SHELL


        endor2.vm.provision "file", source: "endor2/bind9", destination: "/tmp/bind9"
        endor2.vm.provision "shell", inline: "mv /tmp/bind9 /etc/default/bind9"

        endor2.vm.provision "shell", inline: "sudo systemctl restart bind9"

        endor2.vm.provision "file", source: "endor2/named.conf.options", destination: "/tmp/named.conf.options"
        endor2.vm.provision "shell", inline: "mv /tmp/named.conf.options /etc/bind/named.conf.options"

        endor2.vm.provision "file", source: "endor2/named.conf.local", destination: "/tmp/named.conf.local"
        endor2.vm.provision "shell", inline: "mv /tmp/named.conf.local /etc/bind/named.conf.local"


        endor2.vm.provision "file", source: "endor1/00-private-nameservers.yaml", destination: "/tmp/00-private-nameservers.yaml"
        endor2.vm.provision "shell", inline: "mv /tmp/00-private-nameservers.yaml /etc/netplan/00-private-nameservers.yaml"

          endor2.vm.provider :virtualbox do |endor2|
              endor2.customize ["modifyvm", :id, "--memory", "1024"]
              endor2.customize ["modifyvm", :id, "--cpus", "1"]
          end

    end

end