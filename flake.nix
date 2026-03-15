{
  description = "Clipboard Sync - Cross-device clipboard sharing with Spring Boot";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs = { self, nixpkgs, flake-utils }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = nixpkgs.legacyPackages.${system};
        jdk = pkgs.jdk17;
      in
      {
        devShells.default = pkgs.mkShell {
          buildInputs = with pkgs; [
            # Java development
            jdk
            maven
            
            # Language Server for Java (for Emacs lsp-mode or eglot)
            jdt-language-server
            
            # Clipboard tools
            xclip
            
            # Development tools
            git
            curl
            jq
            httpie
            
            # Code formatting (optional but nice)
            google-java-format  # Java formatter
          ];
          
          shellHook = ''
            echo "🚀 Clipboard Sync Development Environment"
            echo "📦 Java version: $(java -version 2>&1 | head -n 1)"
            echo "📦 Maven version: $(mvn -version | head -n 1)"
            echo "📦 Language Server: jdt-language-server available"
            echo ""
            echo "Emacs users:"
            echo "  - LSP server is in PATH"
            echo "  - Use lsp-mode or eglot for IDE features"
            echo ""
            echo "Available commands:"
            echo "  mvn spring-boot:run  - Run the application"
            echo "  mvn clean install    - Build the project"
            echo "  mvn test             - Run tests"
            echo ""
            
            export JAVA_HOME="${jdk}"
            mkdir -p data
            
            # Make LSP server discoverable
            export PATH="${pkgs.jdt-language-server}/bin:$PATH"
          '';
        };
        
        packages.default = pkgs.stdenv.mkDerivation {
          pname = "clipboard-sync";
          version = "0.1.0";
          
          src = ./.;
          
          nativeBuildInputs = with pkgs; [ jdk maven ];
          
          buildPhase = ''
            mvn clean package -DskipTests
          '';
          
          installPhase = ''
            mkdir -p $out/bin $out/lib
            cp target/*.jar $out/lib/clipboard-sync.jar
            
            cat > $out/bin/clipboard-sync <<EOF
            #!${pkgs.bash}/bin/bash
            exec ${jdk}/bin/java -jar $out/lib/clipboard-sync.jar "\$@"
            EOF
            
            chmod +x $out/bin/clipboard-sync
          '';
        };
      }
    );
}
