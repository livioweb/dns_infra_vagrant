
# -*- mode: ruby -*-
# vi: set ft=ruby :
VAGRANTFILE_API_VERSION = "2"

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|

  config.ssh.insert_key = true
  #config.disksize.size = '20GB'

  config.vm.define :bb8 do |bb8|

    bb8.vm.provider :virtualbox do |v|
        v.name = "bb8"
        v.customize [
            "modifyvm", :id,
            "--name", "bb8",
            "--memory", 512,
            "--natdnshostresolver1", "on",
            "--cpus", 1,
        ]
    end

    bb8.vm.box = "ubuntu/bionic64"
    bb8.vm.network :private_network, ip: "172.17.177.200"
    bb8.ssh.forward_agent = true
    bb8.vm.synced_folder "./", "/vagrant", :nfs => true
    bb8.vm.hostname = "bb8"
    bb8.vm.provision "shell", path: "bb8/script.sh"

    bb8.vm.provision "ansible" do |bb8|
        bb8.verbose = "v"
        bb8.compatibility_mode = "2.0"
        bb8.playbook = "bb8/provisioning/playbook.yml"
        bb8.inventory_path = "bb8/provisioning/inventory"
        bb8.become = true
        bb8.limit = "bb8"
        bb8.extra_vars = {
            node_ip: "172.17.177.200",
        }
    end

end

(1..3).each do |i|
    config.vm.define "xwing_#{i}" do |xwings|
        xwings.vm.provider :virtualbox do |v|
            v.name = "xwing_#{i}"
            v.customize [
                "modifyvm", :id,
                "--name", "xwing-#{i}",
                "--memory", 512,
                "--natdnshostresolver1", "on",
                "--cpus", 1,
            ]
        end
        xwings.vm.box = "ubuntu/bionic64"
        xwings.vm.network :private_network, ip: "172.17.177.20#{i}"
        xwings.ssh.forward_agent = true
        xwings.vm.synced_folder "./", "/vagrant", :nfs => true, :mount_options => ['vers=3','noatime','actimeo=2', 'tcp', 'fsc']
        xwings.vm.hostname = "xwing-#{i}"
        xwings.vm.provision "shell", path: "xwings/script.sh"

        xwings.vm.provision "ansible" do |xwings|
            xwings.verbose = "v"
            xwings.compatibility_mode = "2.0"
            xwings.playbook = "xwings/provisioning/playbook.yml"
            xwings.inventory_path = "xwings/provisioning/inventory"
            xwings.become = true
            xwings.limit = "xwings"
            xwings.extra_vars = {
                node_ip: "172.17.177.20#{i}",
            }
        end

    xwings.vm.provision "shell", inline: <<-SHELL
      echo "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDoXmBHMI+z1YOx+B/tSWQu8yF5WgIRjdxvTkEOx+I+U6MCL6YnvyJgMAG4gSAh8I9/pAiOpn/JpmXYgwhgWBV6yr8zon4hd5qJM9XJK6MlKJwDD4ag6Qtg/orG3gflf7KObINpjrwyq+LphJf/evs7z34JQoX1qzqg3SkFtesjs9s/qMykwpTaDe4Fr31FMCdyuZEebL51fGBjUZT9XmW0hBKPedaQdGWWpYYRQ1wSIBEt20aWhu1JfdKrr498wjbdLcCOqQ4cS07UcroH7wRPQp/NjYKMD3xYvPFvCUg/BuGK8cenH+aH3Uv9AjYS5CyAYYbNbRN+SzdOaQohUzQv anakin@r2d2" >> /home/vagrant/.ssh/authorized_keys
      echo "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQCv/qDz53vlV7ix5npgjcDr+VZNTlGuhnQzEZxfL833fMq3Jq2MhFk5lAsG6wVtkLlfO899aF8jinXsYPPqqjSE/f7vrSiBE+1UwgvAWuzie8GmT3/4Zbbxl8VGOU7T3vUDxl1NCUOxXx+ewSMwx8uUpfcUwJ1A1FQKnIcoeDZiOjo+lRWKWankN/MlCzU+YdZVLxWcw8qLFSeNqaj+WT0bZpI2lYyJLQiamjL6Izh56zsY1njqc4QZxXiUiLpeMc3kw1mcs6d0KpTLxTaYP/fpfI4nMccNTkHudKA/aXQDD8+iwIs7WEnZY8OybP9g2stUH/L7i3CYotKKbHmUNihD jenkins@jenkins.docker" >> /home/vagrant/.ssh/authorized_keys
    SHELL


    end
end  


  config.vm.define "sonar" do |sonar|
    sonar.vm.box = "ubuntu/bionic64"
    sonar.vm.network "private_network", ip: "172.17.177.140"

    sonar.vm.hostname = "sonar"
    sonar.vm.synced_folder "sonar/", "/home/vagrant/sonar"
    sonar.vm.provision "shell", path: "sonar/script.sh"

        sonar.vm.provider :virtualbox do |sonar|
            sonar.customize ["modifyvm", :id, "--memory", "2048"]
            sonar.customize ["modifyvm", :id, "--cpus", "2"]
        end

        sonar.vm.provision "ansible" do |sonar|
            sonar.verbose = "vv"
            sonar.compatibility_mode = "2.0"
            sonar.playbook = "sonar/provisioning/playbook.yml"
            sonar.inventory_path = "sonar/provisioning/inventory"
            sonar.become = true
            sonar.limit = "sonar"
         end
  end

  config.vm.define "gitlab" do |gitlab|
    gitlab.vm.box = "ubuntu/bionic64"
    gitlab.vm.network "private_network", ip: "172.17.177.120"

    gitlab.vm.hostname = "gitlab"
    gitlab.vm.synced_folder "gitlab/", "/home/vagrant/gitlab"
    gitlab.vm.provision "shell", path: "gitlab/script.sh"
        gitlab.vm.provider :virtualbox do |gitlab|
            gitlab.customize ["modifyvm", :id, "--memory", "8096"]
            gitlab.customize ["modifyvm", :id, "--cpus", "4"]
        end

        gitlab.vm.provision "ansible" do |gitlab|
            gitlab.verbose = "v"
            gitlab.compatibility_mode = "2.0"
            gitlab.playbook = "gitlab/provisioning/playbook.yml"
            gitlab.inventory_path = "gitlab/provisioning/inventory"
            gitlab.become = true
            gitlab.limit = "gitlab"
         end
  end


    config.vm.define "jenkins" do |jenkins|
      jenkins.vm.box = "ubuntu/bionic64"
      jenkins.vm.network "private_network", ip: "172.17.177.130"
  
      jenkins.vm.hostname = "jenkins"
      jenkins.vm.synced_folder "jenkins/", "/home/vagrant/jenkins"
      jenkins.vm.provision "shell", path: "jenkins/script.sh"
  
          jenkins.vm.provider :virtualbox do |jenkins|
              jenkins.customize ["modifyvm", :id, "--memory", "4096"]
              jenkins.customize ["modifyvm", :id, "--cpus", "4"]
          end
  
          jenkins.vm.provision "ansible" do |jenkins|
              jenkins.verbose = "vv"
              jenkins.compatibility_mode = "2.0"
              jenkins.playbook = "jenkins/provisioning/playbook.yml"
              jenkins.inventory_path = "jenkins/provisioning/inventory"
              jenkins.become = true
              jenkins.limit = "jenkins"
           end
    end



  config.vm.define "endor1" do |endor1|
      endor1.vm.box = "ubuntu/bionic64"
      endor1.vm.network "private_network", ip: "172.17.177.53"


      endor1.vm.provision "shell", inline: <<-SHELL
      export DEBIAN_FRONTEND=noninteractive
      apt-get update
      sudo apt-get install -y bind9 bind9utils bind9-doc
      #sudo groupadd endor
      #sudo usermod -aG bind vagrant
      #sudo usermod -aG bind ${USER}
      SHELL

      #endor1.disksize.size = '2GB'
      endor1.vm.hostname = "endor1"
      endor1.vm.synced_folder "endor1/", "/home/vagrant/endor1"
      endor1.vm.provision "shell", path: "endor1/script.sh"


        endor1.vm.provision "shell", inline: <<-SHELL
           # rm /etc/bind/named.conf.options
           # rm /etc/bind/named.conf.local
           # rm /etc/default/bind9
           # rm -r /etc/bind/zones
           # rm /etc/netplan/00-private-nameservers.yaml
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


      endor2.vm.provision "shell", inline: <<-SHELL
      export DEBIAN_FRONTEND=noninteractive
      apt-get update
      sudo apt-get install -y bind9 bind9utils bind9-doc
      SHELL
     

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