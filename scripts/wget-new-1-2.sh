#!/bin/bash
##############################################################################
# ESG Federation download script
#
# Template version: 1.2
# Generated by esgf-index1.ceda.ac.uk - 2014/09/02 07:14:07
# Search URL: http://esgf-index1.ceda.ac.uk/esg-search/wget/?query=*&dataset_id=pmip3.output.UOED.HadCM3.past1000.mon.land.Lmon.r1i1p1.v20130313|esgf-data1.ceda.ac.uk
#
###############################################################################
# first be sure it's bash... anything out of bash or sh will break
# and the test will assure we are not using sh instead of bash
if [ $BASH ] && [ `basename $BASH` != bash ]; then
    echo "######## This is a bash script! ##############" 
    echo "Change the execution bit 'chmod u+x $0' or start with 'bash $0' instead of sh."
    echo "Trying to recover automatically..."
    sleep 1
    /bin/bash $0 $@
    exit $?
fi

version=1.3.2
CACHE_FILE=.$(basename $0).status
openId=
search_url='http://esgf-index1.ceda.ac.uk/esg-search/wget/?query=*&dataset_id=pmip3.output.UOED.HadCM3.past1000.mon.land.Lmon.r1i1p1.v20130313|esgf-data1.ceda.ac.uk'

#These are the embedded files to be downloaded
#download_files="$(cat <<EOF--dataset.file.url.chksum_type.chksum
#'evspsblveg_Lmon_HadCM3_past1000_r1i1p1_085001-185012.nc' 'http://esgf-data1.ceda.ac.uk/thredds/fileServer/esg_dataroot/pmip3/output/UOED/HadCM3/past1000/mon/land/#Lmon/r1i1p1/v20130313/evspsblveg/evspsblveg_Lmon_HadCM3_past1000_r1i1p1_085001-185012.nc' 'MD5' 'a373a192f4e4108de42ab4b4a9f699ee'
#EOF--dataset.file.url.chksum_type.chksum
#)"

#download_files="$(cat <<EOF--dataset.file.url.chksum_type.chksum
#'evspsblveg_Lmon_HadCM3_past1000_r1i1p1_085001-185012.nc' 'http://esgf-data1.ceda.ac.uk/thredds/fileServer/esg_dataroot/pmip3/output/UOED/HadCM3/past1000/mon/land/#Lmon/r1i1p1/v20130313/evspsblveg/evspsblveg_Lmon_HadCM3_past1000_r1i1p1_085001-185012.nc' 'MD5' 'a373a192f4e4108de42ab4b4a9f699ee'
#EOF--dataset.file.url.chksum_type.chksum
#)"

#'gpp_Lmon_HadCM3_past1000_r1i1p1_085001-185012.nc' 'http://esgf-data1.ceda.ac.uk/thredds/fileServer/esg_dataroot/pmip3/output/UOED/HadCM3/past1000/mon/land/Lmon/r1i1p1/v20130313/gpp/gpp_Lmon_HadCM3_past1000_r1i1p1_085001-185012.nc' 'MD5' '72ce15ef29cdf28ffc897d7242f218f1'
#'evspsblveg_Lmon_HadCM3_past1000_r1i1p1_085001-185012.nc' 'http://esgf-data1.ceda.ac.uk/thredds/fileServer/esg_dataroot/pmip3/output/UOED/HadCM3/past1000/mon/land/Lmon/r1i1p1/v20130313/evspsblveg/evspsblveg_Lmon_HadCM3_past1000_r1i1p1_085001-185012.nc' 'MD5' 'a373a192f4e4108de42ab4b4a9f699ee'
#'gpp_Lmon_HadCM3_past1000_r1i1p1_085001-185012.nc' 'http://esgf-data1.ceda.ac.uk/thredds/fileServer/esg_dataroot/pmip3/output/UOED/HadCM3/past1000/mon/land/Lmon/r1i1p1/v20130313/gpp/gpp_Lmon_HadCM3_past1000_r1i1p1_085001-185012.nc' 'MD5' '72ce15ef29cdf28ffc897d7242f218f1'
#'evspsblveg_Lmon_HadCM3_past1000_r1i1p1_085001-185012.nc' 'http://esgf-data1.ceda.ac.uk/thredds/fileServer/esg_dataroot/pmip3/output/UOED/HadCM3/past1000/mon/land/Lmon/r1i1p1/v20130313/evspsblveg/evspsblveg_Lmon_HadCM3_past1000_r1i1p1_085001-185012.nc' 'MD5' 'a373a192f4e4108de42ab4b4a9f699ee'
#'gpp_Lmon_HadCM3_past1000_r1i1p1_085001-185012.nc' 'http://esgf-data1.ceda.ac.uk/thredds/fileServer/esg_dataroot/pmip3/output/UOED/HadCM3/past1000/mon/land/Lmon/r1i1p1/v20130313/gpp/gpp_Lmon_HadCM3_past1000_r1i1p1_085001-185012.nc' 'MD5' '72ce15ef29cdf28ffc897d7242f218f1'
#'evspsblveg_Lmon_HadCM3_past1000_r1i1p1_085001-185012.nc' 'http://esgf-data1.ceda.ac.uk/thredds/fileServer/esg_dataroot/pmip3/output/UOED/HadCM3/past1000/mon/land/Lmon/r1i1p1/v20130313/evspsblveg/evspsblveg_Lmon_HadCM3_past1000_r1i1p1_085001-185012.nc' 'MD5' 'a373a192f4e4108de42ab4b4a9f699ee'




download_files="$(cat <<EOF--dataset.file.url.chksum_type.chksum
'sftlf.nc' 'https://esgf-test1.ceda.ac.uk/thredds/fileServer/esg_dataroot/test/sftlf.nc' 'MD5' 'a373a192f4e4108de42ab4b4a9f699ee'
EOF--dataset.file.url.chksum_type.chksum
)"



# ESG_HOME should point to the directory containing ESG credentials.
#   Default is $HOME/.esg
ESG_HOME=${ESG_HOME:-$HOME/.esg}
[[ -d $ESG_HOME ]] || mkdir -p $ESG_HOME

ESG_CREDENTIALS=${X509_USER_PROXY:-$ESG_HOME/credentials.pem}
ESG_CERT_DIR=${X509_CERT_DIR:-$ESG_HOME/certificates}
MYPROXY_STATUS=$HOME/.MyProxyLogon
TRUSTSTORE=$ESG_HOME/esg-truststore.ts
COOKIE_JAR=$ESG_HOME/cookies
MYPROXY_GETCERT=$ESG_HOME/getcert.jar
CERT_EXPIRATION_WARNING=$((60 * 60 * 8))   #Eight hour (in seconds)

WGET_TRUSTED_CERTIFICATES=$ESG_HOME/certificates


# Configure checking of server SSL certificates.
#   Disabling server certificate checking can resolve problems with myproxy
#   servers being out of sync with datanodes.
CHECK_SERVER_CERT=${CHECK_SERVER_CERT:-Yes}

check_os() {
    local os_name=$(uname | awk '{print $1}')
    case ${os_name} in
        Linux)
            ((debug)) && echo "Linux operating system detected"
            LINUX=1
            MACOSX=0
            ;;
        Darwin)
            ((debug)) && echo "Mac OS X operating system detected"
            LINUX=0
            MACOSX=1
            ;;
        *)
            echo "Unrecognized OS [${os_name}]"
            return 1
            ;;
    esac
    return 0
}

#taken from http://stackoverflow.com/a/4025065/1182464
vercomp () {
    if [[ $1 == $2 ]]
    then
        return 0
    fi
    local IFS=.
    local i ver1=($1) ver2=($2)
    # fill empty fields in ver1 with zeros
    for ((i=${#ver1[@]}; i<${#ver2[@]}; i++))
    do
        ver1[i]=0
    done
    for ((i=0; i<${#ver1[@]}; i++))
    do
        if [[ -z ${ver2[i]} ]]
        then
            # fill empty fields in ver2 with zeros
            ver2[i]=0
        fi
        if ((10#${ver1[i]} > 10#${ver2[i]}))
        then
            return 1
        fi
        if ((10#${ver1[i]} < 10#${ver2[i]}))
        then
            return 2
        fi
    done
    return 0
}

check_commands() {
    #check wget
    local MIN_WGET_VERSION=1.10
    vercomp $(wget -V | sed -n 's/^.* \([1-9]\.[0-9.]*\) .*$/\1/p') $MIN_WGET_VERSION
    case $? in
        2) #lower
            wget -V
            echo
            echo "** ERROR: wget version is too old. Use version $MIN_WGET_VERSION or greater. **" >&2
            exit 1
    esac
}

usage() {
    echo "Usage: $(basename $0) [flags]"
    echo "Flags is one of:"
    sed -n '/^while getopts/,/^done/  s/^\([^)]*\)[^#]*#\(.*$\)/\1 \2/p' $0
    echo
    echo "This command stores the states of the downloads in .$0.status"
    echo "For more information check the website: http://esgf.org/wiki/ESGF_wget"
}

#defaults
debug=0
clean_work=1

#parse flags
while getopts ':c:pfF:o:w:isuUndvqhCN' OPT; do
    case $OPT in
        c) ESG_CREDENTIALS="$OPTARG";;  #<cert> : use this certificate for authentication.
        p) clean_work=0;;               #       : preserve data that failed checksum
        f) force=1;;                    #       : force certificate retrieval (defaults to only once per day)
        F) input_file="$OPTARG";;       #<file> : read input from file instead of the embedded one (use - to read from stdin)
        o) openId="$OPTARG";;           #<openid>: Provide OpenID instead of interactively asking for it.
        w) output="$OPTARG";;           #<file> : Write embedded files into a file and exit
        i) insecure=1;;                 #       : set insecure mode, i.e. don't check server certificate
        s) skip_security=1;;            #       : completely skip security. It will only work if the accessd data is not secured at all.
        u) update=1;;                   #       : Issue the search again and see if something has changed.
        U) update_files=1;;             #       : Update files from server overwriting local ones (detect with -u)
        n) dry_run=1;;                  #       : Don't download any files, just report.
        d) verbose=1;debug=1;;          #       : display debug information
        v) verbose=1;;                  #       : be more verbose
        q) quiet=1;;                    #       : be less verbose
        C) skip_security=1 && use_cks=1;; #      : Do not use certificates for security but use cookies
        h) usage && exit 0;;            #       : displays this help
        \?) echo "Unknown option '$OPTARG'" >&2 && usage && exit 1;;
        \:) echo "Missing parameter for flag '$OPTARG'" >&2 && usage && exit 1;;
    esac
done
shift $(($OPTIND - 1))

#setup input as desired by the user
if [[ "$input_file" ]]; then
    if [[ "$input_file" == '-' ]]; then
        download_files="$(cat)" #read from STDIN
        exec 0</dev/tty #reopen STDIN as cat closed it
    else
        download_files="$(cat $input_file)" #read from file
    fi
fi

#if -w (output) was selected write file and finish:
if [[ "$output" ]]; then
    #check the file
    if [[ -f "$output" ]]; then
        read -p "Overwrite existing file $output? (y/N) " answ
        case $answ in y|Y|yes|Yes);; *) echo "Aborting then..."; exit 0;; esac
    fi
    echo "$download_files">$output
    exit
fi


#assure we have everything we need
check_commands

if ((update)); then
    echo "Checking the server for changes..."
    new_wget="$(wget "$search_url" -qO -)"
    compare_cmd="grep -vE '^(# Generated by|# Search URL|search_url=)'"
    if diff -q <(eval $compare_cmd<<<"$new_wget") <(eval $compare_cmd $0) >/dev/null; then
        echo "No changes detected."
    else
        echo "Wget was changed. Dowloading. (old renamed to $0.old.#N)"
        counter=0
        while [[ -f $0.old.$counter ]]; do ((counter++)); done
        mv $0 $0.old.$counter
        echo "$new_wget" > $0
    fi
    exit 0      
fi


##############################################################################
check_java() {
    if ! type java >& /dev/null; then
        echo "Java is required for retrieving the certificate and couldn't be found."
        exit 1
    fi
    jversion=($(jversion=$(java -version 2>&1 | awk '/version/ {gsub("\"","");print $3}'); echo ${jversion//./ }))
    mVer=${jversion[1]}
    if ((mVer<5)); then
        echo "Java version 1.5+ is required for retrieving the certificate." >&2
        echo "Current version seems older: $(java -version | head -n1) " >&2  
        exit 1
    fi
}

proxy_to_java() {
    local proxy_user proxy_pass proxy_server proxy_port
    eval $(sed 's#^\(https\?://\)\?\(\([^:@]*\)\(:\([^@]*\)\)\?@\)\?\([^:/]*\)\(:\([0-9]*\)\)\?.*#proxy_user=\3;proxy_pass=\5;proxy_server=\6;proxy_port=\8#'<<<$http_proxy)
    local JAVA_PROXY=
    [[ "$proxy_server" ]] && JAVA_PROXY=$JAVA_PROXY" -Dhttp.proxyHost=$proxy_server"
    [[ "$proxy_port" ]] && JAVA_PROXY=$JAVA_PROXY" -Dhttp.proxyPort=$proxy_port"
    eval $(sed 's#^\(https\?://\)\?\(\([^:@]*\)\(:\([^@]*\)\)\?@\)\?\([^:/]*\)\(:\([0-9]*\)\)\?.*#proxy_user=\3;proxy_pass=\5;proxy_server=\6;proxy_port=\8#'<<<$https_proxy)
    [[ "$proxy_server" ]] && JAVA_PROXY=$JAVA_PROXY" -Dhttps.proxyHost=$proxy_server"
    [[ "$proxy_port" ]] && JAVA_PROXY=$JAVA_PROXY" -Dhttps.proxyPort=$proxy_port"
    
    echo "$JAVA_PROXY"
}

# get certificates from github
get_certificates() {
    # don't if this was already done today
    [[ -z $force && "$(find $ESG_CERT_DIR -type d -mtime -1 2>/dev/null)" ]] && return 0
    echo -n "Retrieving Federation Certificates..." >&2
    
    if ! wget --no-check-certificate https://raw.githubusercontent.com/ESGF/esgf-dist/master/installer/certs/esg_trusted_certificates.tar -O - -q | tar x -C $ESG_HOME; then
        #certificates tarred into esg_trusted_certificates. (if it breaks, let the user know why
        wget --no-check-certificate https://raw.githubusercontent.com/ESGF/esgf-dist/master/installer/certs/esg_trusted_certificates.tar
        echo "Could't update certs!" >&2
        return 1
    else
        #if here everythng went fine. Replace old cert with this ones    
        [[ -d $ESG_CERT_DIR ]] && rm -r $ESG_CERT_DIR || mkdir -p $(dirname $ESG_CERT_DIR)
        mv $ESG_HOME/esg_trusted_certificates $ESG_CERT_DIR
        touch $ESG_CERT_DIR
        echo "done!" >&2
    fi

}

#get truststore from github
get_truststore() {
    # don't if this was already done today
    [[ -z $force && "$(find $TRUSTSTORE -type f -mtime -1 2>/dev/null)" ]] && return 0
    echo -n "Retrieving Federation Truststore..." >&2
    
    if ! wget --no-check-certificate https://raw.githubusercontent.com/ESGF/esgf-dist/master/installer/certs/esg-truststore.ts -O $TRUSTSTORE -q ; then
        #certificates tarred into esg_trusted_certificates. (if it breaks, let the user know why
        wget --no-check-certificate https://raw.githubusercontent.com/ESGF/esgf-dist/master/installer/certs/esg-truststore.ts -O $TRUSTSTORE
        echo "Could't update truststore!" >&2
        return 1
    else
        touch $TRUSTSTORE
    fi
    echo "done!" >&2    
}

# Retrieve ESG credentials
unset pass
get_credentials() {
    check_java
    #get all certificates
    get_certificates
    #get truststore 
    get_truststore
    
    if [[ -z "$(find $MYPROXY_GETCERT -type f -mtime -1 2>/dev/null)" ]]; then
        echo -n "(Downloading $MYPROXY_GETCERT... "
        mkdir -p $(dirname $MYPROXY_GETCERT)
        if wget -q --no-check-certificate https://raw.githubusercontent.com/ESGF/esgf-dist/master/installer/certs/getcert.jar -O $MYPROXY_GETCERT;then
            echo 'done)'
            touch $MYPROXY_GETCERT
        else
            echo 'failed)'
        fi
    fi

    #if the user already defined one, use it
    if [[ -z $openId ]]; then
        #try to parse the last valid value if any
        [[ -f "$MYPROXY_STATUS" ]] && openId=$(awk -F= '/^OpenID/ {gsub("\\\\", ""); print $2}' $MYPROXY_STATUS)
        if [[ -z $openId ]]; then
            #no OpenID, we need to ask the user
            echo -n "Please give your OpenID (Example: https://myserver/example/username) ? "
        else
            #Allow the user to change it if desired
            echo -n "Please give your OpenID (hit ENTER to accept default: $openId)? "
        fi
        read -e
        [[ "$REPLY" ]] && openId="$REPLY"
    else
        ((verbose)) && echo "Using user defined OpenID $openId (to change use -o <open_id>)"
    fi

    if grep -q ceda.ac.uk <<<$openId; then
        username=${openId##*/}
        echo -n "Please give your username if different [$username]: "
        read -e
        [[ "$REPLY" ]] && username="$REPLY"
    fi
    
    #get password
    [[ ! "$pass" ]] && read -sp "MyProxy Password? " pass


    local args=
    [[ "$openId" ]] && args=$args" --oid $openId"
    [[ "$pass" ]] && args=$args" -P $pass"
    [[ "$username" ]] && args=$args" -l $username"
    
    echo -n $'\nRetrieving Credentials...' >&2
    if ! java $(proxy_to_java) -Djavax.net.ssl.trustStore=$TRUSTSTORE -Djavax.net.ssl.trustStorePassword=changeit -jar $MYPROXY_GETCERT $args --output $ESG_CREDENTIALS ; then        
        echo "Certificate could not be retrieved"
        exit 1
    fi
    echo "done!" >&2
}

# check the certificate validity
check_cert() {
    if [[ ! -f "$ESG_CERT" || $force ]]; then
        #not there, just get it
        get_credentials
    elif which openssl &>/dev/null; then
        #check openssl and certificate
        if ! openssl x509 -checkend $CERT_EXPIRATION_WARNING -noout -in $ESG_CERT 2>/dev/null; then
            echo "The certificate expires in less than $((CERT_EXPIRATION_WARNING / 60 / 60)) hour(s). Renewing..."
            get_credentials
        else
            #ok, certificate is fine
            return 0
        fi
    fi
}

#
# Detect ESG credentials
#
find_credentials() {

    #is X509_USER_PROXY or $HOME/.esg/credential.pem
    if [[ -f "$ESG_CREDENTIALS" ]]; then
        # file found, proceed.
        ESG_CERT="$ESG_CREDENTIALS"
        ESG_KEY="$ESG_CREDENTIALS"
    elif [[ -f "$X509_USER_CERT" && -f "$X509_USER_KEY" ]]; then
        # second try, use these certificates.
        ESG_CERT="$X509_USER_CERT"
        ESG_KEY="$X509_USER_KEY"
    else
        # If credentials are not present, just point to where they should go 
        echo "No ESG Credentials found in $ESG_CREDENTIALS" >&2
            ESG_CERT="$ESG_CREDENTIALS"
            ESG_KEY="$ESG_CREDENTIALS"
            #they will be retrieved later one
    fi


    #chek openssl and certificate
    if (which openssl &>/dev/null); then
        if ( openssl version | grep 'OpenSSL 1\.0' ); then
            echo '** WARNING: ESGF Host certificate checking might not be compatible with OpenSSL 1.0+'
        fi
        check_cert || { (($?==1)); exit 1; }
    fi
    
    if [[ $CHECK_SERVER_CERT == "Yes" ]]; then
        [[ -d "$ESG_CERT_DIR" ]] || { echo "CA certs not found. Aborting."; exit 1; }
        PKI_WGET_OPTS="--ca-directory=$ESG_CERT_DIR"
    fi

    #some wget version complain if there's no file present
    [[ -f $COOKIE_JAR ]] || touch $COOKIE_JAR

    PKI_WGET_OPTS="$PKI_WGET_OPTS --certificate=$ESG_CERT --private-key=$ESG_KEY --save-cookies=$COOKIE_JAR --load-cookies=$COOKIE_JAR --ca-certificate=$ESG_CERT"

}

check_chksum() {
    local file="$1"
    local chk_type=$2
    local chk_value=$3
    local local_chksum=Unknown

    case $chk_type in
        md5) local_chksum=$(md5sum_ $file | cut -f1 -d" ");;
        sha256) local_chksum=$(sha256sum $file|awk '{print $1}'|cut -d ' ' -f1);;
        *) echo "Can't verify checksum." && return 0;;
    esac

    #verify
    ((debug)) && echo "local:$local_chksum vs remote:$chk_value" >&2
    echo $local_chksum
}

#Our own md5sum function call that takes into account machines that don't have md5sum but do have md5 (i.e. mac os x)
md5sum_() {
    hash -r
    if type md5sum >& /dev/null; then
        echo $(md5sum $@)
    else
        echo $(md5 $@ | sed -n 's/MD5[ ]*\(.*\)[^=]*=[ ]*\(.*$\)/\2 \1/p')
    fi
}

get_mod_time_() {
    if ((LINUX)); then
        #on linux modtime is stat -c %Y <file>
        echo "$(stat -c %Y $@)"
    elif ((MACOSX)); then
        #on a mac modtime is stat -f %m <file>
        echo "$(stat -f %m $@)"
    fi
    return 0;
}

remove_from_cache() {
    local entry="$1"
    local tmp_file="$(grep -ve "^$entry" "$CACHE_FILE")"
    echo "$tmp_file" > "$CACHE_FILE"
    unset cached
}

debug_duc=0
debug_duc_1=0
download_using_cookies()
{
  #Download the certificates for trusted nodes if needed. This will be used in wget.
  if [ -z "$insecure" ]
   then 
    get_certificates
  fi
  
  #Create cookies folder.
  COOKIES_FOLDER="$ESG_HOME/wget_cookies"
  if [ ! -d $COOKIES_FOLDER ] 
   then
    mkdir $COOKIES_FOLDER
  fi 

  #The data to be downloaded.
  data=" $url"
  filename="$file"  

  #Wget args.
  if [ -z "$insecure" ] 
   then
    wget_args=" --ca-directory=$WGET_TRUSTED_CERTIFICATES --cookies=on --keep-session-cookies --save-cookies $COOKIES_FOLDER/wcookies.txt --load-cookies $COOKIES_FOLDER/wcookies.txt -o res "
   else
    wget_args=" --no-check-certificate --cookies=on --keep-session-cookies --save-cookies $COOKIES_FOLDER/wcookies.txt --load-cookies $COOKIES_FOLDER/wcookies.txt -o res "
  fi 
   
  #Debug message.
  if [ $debug_duc_1 -eq 1 ]
   then
    echo -e "executing:\n"
    echo -e "wget $wget_args $data\n"
  fi

  #Try to download the data. 
  command="wget $wget_args $data"
  eval $command 

  #Get the result
  http_resp=$(cat res)
  rm res  

  #Debug message.
  if [ $debug_duc -eq 1 ]
   then
    echo -e "Response is:\n"
    echo $http_resp  
  fi 
  
  #Evaluate response.
  if echo "$http_resp" | grep -q " 302 "  
   then
    redirected=1
    urls=$(echo $http_resp | egrep -o 'https?://[^ ]+'| cut -d'/' -f 3)
    orp_service=$(echo $urls | cut -d' ' -f 3)
    if [ -n "$orp_service" ] 
     then
      orp_service_found=1
     else
      orp_service_found=0
    fi
   else
    redirected=0
    orp_service_found=0
  fi 
  
  #If redirected to orp service send the openid. 
  if [[ $redirected -eq 1 ]] && [[ $orp_service_found -eq 1 ]]
   then

    #Debug message.
    if [ $debug_duc_1 -eq 1 ]
     then
      echo -e "Orp service:\n"
      echo $orp_service
    fi


    #-O $filename
    #Http request for senting openid to the orp web service.
    command="wget --post-data \"openid_identifier=$openid_c&rememberOpenid=on\"  --header=\"Agent-type:cl\" $wget_args  https://$orp_service/esg-orp/j_spring_openid_security_check.htm "

    #Debug message.
    if [ $debug_duc_1 -eq 1 ]
     then
      echo -e "Executing:\n"
      echo -e "$command\n"
    fi

    #Execution of command.
    eval $command #|| { failed=1; break; }

    http_resp=$(cat res)
    rm res

    #Debug message.
    if [ $debug_duc -eq 1 ]
     then
      echo $http_resp
    fi

    #Evaluate response.
    if echo "$http_resp" | grep -q " 302 " && echo "$http_resp" | grep -q "login"  
     then
      redirected=1
      urls=$(echo $http_resp | egrep -o 'https?://[^ ]+' | cut -d'/' -f 3)
      idp_service=$(echo $urls | cut -d' ' -f 2) 
      if [ -n "$idp_service" ] 
       then
        idp_service_found=1
      else
       idp_service_found=0
      fi
     else
      redirected=0
      idp_service_found=0
    fi

   #If redirected to idp service send the credentials.
   if [[ $redirected -eq 1 ]] && [[ $idp_service_found -eq 1 ]]   
    then
          
     #Location of orp.
     if [ $debug_duc_1 -eq 1 ]
      then
      echo -e "Idp service:\n"
      echo $urls 
      echo $idp_service
     fi
 
     #Create the Http request for the idp web service.
     if echo "$http_resp" | grep -q "login.htm";
      then 
       command="wget --post-data  \"password=$password_c\" --header=\"Agent-type:cl\" --auth-no-challenge --http-user=$username_c --http-password=$password_c $wget_args -O $filename https://$idp_service/esgf-idp/idp/login.htm"
      else
       command="wget --post-data  \"password=$password_c\" --header=\"Agent-type:cl\" --auth-no-challenge --http-user=$username_c --http-password=$password_c $wget_args -O $filename https://$idp_service/esgf-idp/idp/login_ids.htm"
     fi
     #

     #Debug message.
     if [ $debug_duc_1 -eq 1 ]
      then
       echo -e "Executing:\n"
       echo -e "$command\n"
     fi
          
     #Execution of command.
     eval $command #|| { failed=1; break; }

     http_resp=$(cat res)
     rm res
     
    if echo "$http_resp" | grep -q "text/html" || echo "$http_resp" | grep -q "403: Forbidden"
     then 
      failed=1;
      rm $filename  
    fi 


     
     #Debug message. 
     if [ $debug_duc_1 -eq 1 ]
      then
       echo $http_resp
     fi 
 
    else
     echo "ERROR : http request to idp service did not send."
     failed=1
   fi #if redirected to idp.
   else
    echo "ERROR : http request to orp service did not send."
    failed=1
  fi #if redirected to orp.
}


download() {
    wget="wget ${insecure:+--no-check-certificate} ${quiet:+-q} ${quiet:--v} -c $PKI_WGET_OPTS"
        
    while read line
    do
        # read csv here document into proper variables
        eval $(awk -F "' '" '{$0=substr($0,2,length($0)-2); $3=tolower($3); print "file=\""$1"\";url=\""$2"\";chksum_type=\""$3"\";chksum=\""$4"\""}' <(echo $line) )

        #Process the file
        echo -n "$file ..."

        #get the cached entry if any.
        cached="$(grep -e "^$file" "$CACHE_FILE")"
        
        #if we have the cache entry but no file, clean it.
        if [[ ! -f $file && "$cached" ]]; then
            #the file was removed, clean the cache
            remove_from_cache "$file"
            unset cached
        fi
        
        #check it wasn't modified
        if [[ -n "$cached" && "$(get_mod_time_ $file)" == $(echo "$cached" | cut -d ' ' -f2) ]]; then
                    if [[ "$chksum" == "$(echo "$cached" | cut -d ' ' -f3)" ]]; then
                echo "Already downloaded and verified"
                continue
            elif ((update_files)); then
                #user want's to overwrite newer files
                rm $file
                remove_from_cache "$file"
                unset cached
            else
                #file on server is different from what we have. 
                echo "WARNING: The remote file was changed (probably a new version is available). Use -U to Update/overwrite"
                continue
            fi
        fi
        unset chksum_err_value chksum_err_count
        
        while : ; do
            # (if we had the file size, we could check before trying to complete)
            echo "Downloading"
            [[ ! -d "$(dirname "$file")" ]] && mkdir -p "$(dirname "$file")"
            if ((dry_run)); then
                #all important info was already displayed, if in dry_run mode just abort
                #No status will be stored
                break
            else
                if [[ $skip_security -eq 1 ]] && [[ $use_cks -eq 1 ]]
                 then
                  download_using_cookies $openid_c $password_c $file $url 
                  if [[ $failed -eq 1 ]]
                   then
                    break
                  fi
                else
                 $wget -O "$file" $url || { failed=1; break; }  
                fi                
            fi

            #check if file is there
            if [[ -f $file ]]; then
                ((debug)) && echo file found
                if [[ ! "$chksum" ]]; then
                    echo "Checksum not provided, can't verify file integrity"
                    break
                fi
                result_chksum=$(check_chksum "$file" $chksum_type $chksum)
                if [[ "$result_chksum" != "$chksum" ]]; then
                    echo "  $chksum_type failed!"
                    if ((clean_work)); then
                        if !((chksum_err_count)); then
                                chksum_err_value=$result_chksum
                                chksum_err_count=2
                            elif ((checksum_err_count--)); then
                                if [[ "$result_chksum" != "$chksum_err_value" ]]; then
                                    #this is a real transmission problem
                                    chksum_err_value=$result_chksum
                                    chksum_err_count=2
                                fi
                            else
                                #ok if here we keep getting the same "different" checksum
                                echo "The file returns always a different checksum!"
                                echo "Contact the data owner to verify what is happening."
                                echo
                                sleep 1
                                break
                            fi
                        
                            rm $file
                            #try again
                            echo -n "  re-trying..."
                            continue
                    else
                            echo "  don't use -p or remove manually."
                    fi
                else
                    echo "  $chksum_type ok. done!"
                    echo "$file" $(get_mod_time_ "$file") $chksum >> $CACHE_FILE
                fi
            fi
            #done!
            break
        done
        
        if ((failed)); then
            echo "download failed"
            # most common failure is certificate expiration, so check this
            #if we have the pasword we can retrigger download
            ((!skip_security)) && [[ "$pass" ]] && check_cert
            unset failed
        fi
        
done <<<"$download_files"

}

dedup_cache_() {
    local file=${1:-${CACHE_FILE}}
    ((debug)) && echo "dedup'ing cache ${file} ..."
    local tmp=$(LC_ALL='C' sort  -r -k1,2 $file | awk '!($1 in a) {a[$1];print $0}' | sort -k2,2)
    ((DEBUG)) && echo "$tmp"
    echo "$tmp" > $file
    ((debug)) && echo "(cache dedup'ed)"
}

#
# MAIN
#
echo "Running $(basename $0) version: $version"
((verbose)) && echo "we use other tools in here, don't try to user their proposed 'options' directly"
echo "Use $(basename $0) -h for help."$'\n'

((debug)) && cat<<EOF
** Debug info **
ESG_HOME=$ESG_HOME
ESG_CREDENTIALS=$ESG_CREDENTIALS
ESG_CERT_DIR=$ESG_CERT_DIR
** -- ** -- ** -- ** --

EOF


cat <<'EOF-MESSAGE'
Script created for 7 file(s)
(The count won't match if you manually edit this file!)



EOF-MESSAGE
sleep 1

check_os
#((!skip_security)) && find_credentials

if [[ $skip_security -ne 1 ]]
then
     find_credentials
else
 if [[ $skip_security -eq 1 ]] && [[ $use_cks -eq 1 ]]
 then    
    read -p    "Enter your openid : " openid_c
    read -p    "Enter username (if not supplied within openid) : " username_c
    read -s -p "Enter password : " password_c
    echo -e "\n"
 fi 
fi
#do we have old results? Create the file if not
[ ! -f $CACHE_FILE ] && echo "#filename mtime checksum" > $CACHE_FILE && chmod 666 $CACHE_FILE

#clean the force parameter if here (at htis point we already have the certificate)
unset force

download

dedup_cache_

#Clean temp data.
if [[ $skip_security -eq 1 ]] && [[ $use_cks -eq 1 ]]
 then 
  if [ -f "j_spring_openid_security_check.htm" ] 
   then
    rm j_spring_openid_security_check.*
  fi

  if [ -d $COOKIES_FOLDER ] 
   then
    rm -rf $COOKIES_FOLDER
  fi
fi

echo "done"
