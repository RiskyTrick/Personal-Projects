//Author- K Bhavesh Naidu
const express = require("express")
const app = express()
const Database = require("@replit/database")
const fs = require("fs")
const db = new Database()
const https = require('https')
const url = 'https://www.reddit.com/r/memes/hot/.json?limit=100'
const ytdl = require('ytdl-core')
const ytSearch = require('yt-search')
const queue = new Map();
const { prefix, token } = require("./config.json");
const stream = ytdl('https://www.youtube.com/watch?v=oJwMiU3RJmA', { filter : 'audioonly' });



app.get("/",(req,res) =>{
  res.send("Hello !")
})

app.listen(3000,()=>{
  console.log("project up and running!")
})
let discord = require("discord.js")
let client = new discord.Client({ intents:["GUILDS","GUILD_MESSAGES"]})

client.on("ready",()=>{
  client.user.setPresence({activity:{name:"with your feelings!"}})
})

//=========================================MUSIC
client.on("message", async message => {
  if (message.author.bot) return;
  if (!message.content.startsWith(prefix)) return;

  const serverQueue = queue.get(message.guild.id);

  if (message.content.startsWith(`${prefix}play`)) {
    execute(message, serverQueue);
    
  } else if (message.content.startsWith(`${prefix}skip`)) {
    skip(message, serverQueue);
    
  } else if (message.content.startsWith(`${prefix}stop`)) {
    stop(message, serverQueue);
    
  } 
});

async function execute(message, serverQueue) {
  const args = message.content.split(" ");

  const voiceChannel = message.member.voice.channel;
  if (!voiceChannel)
    return message.channel.send(
      "You need to be in a voice channel to play music!"
    );
  const permissions = voiceChannel.permissionsFor(message.client.user);
  if (!permissions.has("CONNECT") || !permissions.has("SPEAK")) {
    return message.channel.send(
      "I need the permissions to join and speak in your voice channel!"
    );
  }

  const songInfo = await ytdl.getInfo(args[2]);
  const song = {
        title: songInfo.videoDetails.title,
        url: songInfo.videoDetails.video_url,
   };

  if (!serverQueue) {
    const queueContruct = {
      textChannel: message.channel,
      voiceChannel: voiceChannel,
      connection: null,
      songs: [],
      volume: 5,
      playing: true
    };

    queue.set(message.guild.id, queueContruct);

    queueContruct.songs.push(song);

    try {
      var connection = await voiceChannel.join();
      queueContruct.connection = connection;
      play(message.guild, queueContruct.songs[0]);
    } catch (err) {
      console.log(err);
      queue.delete(message.guild.id);
      return message.channel.send(err);
    }
  } else {
    serverQueue.songs.push(song);
    return message.channel.send(`${song.title} has been added to the queue!`);
  }
}

function skip(message, serverQueue) {
  if (!message.member.voice.channel)
    return message.channel.send(
      "You have to be in a voice channel to skip the music!"
    );
  if (!serverQueue)
    return message.channel.send("There is no song that I could skip!");
  serverQueue.connection.dispatcher.end();
}

function stop(message, serverQueue) {
  if (!message.member.voice.channel)
    return message.channel.send(
      "You have to be in a voice channel to stop the music!"
    );
    
  if (!serverQueue)
    return message.channel.send("There is no song that I could stop!");
    
  serverQueue.songs = [];
  serverQueue.connection.dispatcher.end();
}

function play(guild, song) {
  const serverQueue = queue.get(guild.id);
  if (!song) {
    serverQueue.voiceChannel.leave();
    queue.delete(guild.id);
    return;
  }

  const dispatcher = serverQueue.connection
    .play(ytdl(song.url))
    .on("finish", () => {
      serverQueue.songs.shift();
      play(guild, serverQueue.songs[0]);
    })
    .on("error", error => console.error(error));
  dispatcher.setVolumeLogarithmic(serverQueue.volume / 5);
  message.channel.send(`Start playing: **${song.title}**`);
}
//====================================================== music funcs end

// CODES TO USE IN DIS
client.on('message', async message => {
	// BANK AND BALANCE CODE DECLARATIONS
  let currency = "<:money:854513330215321613>"
  let balance = await db.get(`wallet_${message.author.id}`)
  let bank = await db.get(`bank_${message.author.id}`)
  if(balance === null) balance =0
  if(bank === null) bank = 0
// Join the same voice channel of the author of the message
  if(message.content === "!gf join")
  {
	if (message.member.voice.channel) {
		const connection = await message.member.voice.channel.join();
  }
    
  else{
    message.channel.send("Please Join a Voice Channel")
  }
  }
  //============================================================RECORD
  if( message.content ==="!gf record")
  {
   const voicechannel = message.member.voice.channel;
    if (!voicechannel) return message.channel.send("Please join a voice channel first!");

    const connection = await message.member.voice.channel.join();
    const receiver = connection.receiver.createStream(message.member, {
        mode: "pcm",
        end: "silence"
    });

    const writer = receiver.pipe(fs.createWriteStream(`./recorded-${message.author.id}.pcm`));
    writer.on("finish", () => {
        message.member.voice.channel.leave();
        message.channel.send("Finished writing audio");
    });
  }
  //===================================================================play recorded audio
  if( message.content ==="!gf playback")
  {
   
   const voicechannel = message.member.voice.channel;
    if (!voicechannel) return message.channel.send("Please join a voice channel!");

    if (!fs.existsSync(`./recorded-${message.author.id}.pcm`)) return message.channel.send("Your audio is not recorded!");

    const connection = await message.member.voice.channel.join();
    const stream = fs.createReadStream(`./recorded-${message.author.id}.pcm`);

    const dispatcher = connection.play(stream, {
        type: "converted"
    });

    dispatcher.on("finish", () => {
        message.member.voice.channel.leave();
        return message.channel.send("finished playing audio");
    })
  }
  //========================================================================playback ^ 
   if(message.content==="!gf dis"|| message.content==="!gf disconnect")
   {
      message.member.voice.channel.leave();
        return message.channel.send("Disconnected from Audio channel !!\nThank you for using G-Force Assistant!");
   }

  //=========
  if( message.content ==="!gf music")
  { const voicechannel = message.member.voice.channel;
    if (!voicechannel) return message.channel.send("Please join a voice channel!");
     const connection = await message.member.voice.channel.join();
    connection.play('./audio.mp3');
  }
    if( message.content ==="!gf rules")
  { const voicechannel = message.member.voice.channel;
    if (!voicechannel) return message.channel.send("Please join a voice channel!");
     const connection = await message.member.voice.channel.join();
    connection.play('./rules.mp3');
  }
    if( message.content ==="!gf siege")
  { const voicechannel = message.member.voice.channel;
    if (!voicechannel) return message.channel.send("Please join a voice channel!");
     const connection = await message.member.voice.channel.join();
    connection.play('./siege.mp3');
  }
   
   if( message.content ==="!gf announcement"|| message.content==="!gf ans"|| message.content==="!gf announce")
  { const voicechannel = message.member.voice.channel;
    if (!voicechannel) return message.channel.send("Please join a voice channel!");
     const connection = await message.member.voice.channel.join();
    connection.play('./ans.mp3');
  }
//=============================== Experiment with ans


   

  // BALANCE Code
  if(message.content.startsWith("!gf balance") || message.content.startsWith("!gf bal"))
  {
  
  let moneyEmbed= new discord.MessageEmbed()
  .setTitle(`${message.author.username}'s G-force Token Balance`)
  .setDescription(`Wallet:${balance}${currency}\nBank:${bank}${currency}`)
  .setColor("RANDOM")
  .setFooter("")
  .setThumbnail(message.author.displayAvatarURL({dynamic : true}))
  message.channel.send({embeds:[moneyEmbed]})
   

}
 if(message.content.startsWith("!gf rich") || message.content.startsWith("!gf networth"))
  {
  
  
    let obj = await db.getAll().then(keys => {});
    message.channel.send(obj)
   }

  if(message.content.startsWith("!gf deposit") || message.content.startsWith("!gf dep"))
  {
    if(balance === null || balance === 0){
      let nilbalEmbed= new discord.MessageEmbed()
      .setTitle(`${message.author.username} Your G-force Token Wallet Empty`)
    .setDescription(`Wallet:${balance}${currency}\nBank:${bank}${currency}`)
    .setColor("RED")
    .setFooter("Get some tokens or ask someone to donate !")
    .setThumbnail(message.author.displayAvatarURL({dynamic : true}))
    message.channel.send({embeds:[nilbalEmbed]})
    
    }
    if(balance !== null || balance !== 0)
    {
      var temp1= Number(balance)
      var temp2= Number(bank)
      var temp = temp1+temp2
      db.set(`bank_${message.author.id}`, temp).then(() => {});
      temp= 0;
      temp = temp1-temp1
      db.set(`wallet_${message.author.id}`, temp).then(() => {});
      message.channel.send("Your G-force Tokens have Been Deposited into your Bank Account")

      
    }
  }
    //==================================================== PLAY SONGS


  


  
})

client.on("message",message =>{
  if(message.content === "ping")
    {
      message.channel.send("pong")
    }
     if(message.content === "!gf divide")
    {
      message.channel.send("=================================================================================\n=================================================================================")
    }
    if(message.content === "!gf troll")
    {
      message.channel.send("laugh mothafuka")
    }
    
     if(message.content === "!gf socials")
    {
      message.channel.send("Instagram: \nhttps://www.instagram.com/esports_gitam\n Youtube: \n https://www.youtube.com/channel/UCtlfuefhlsXRF8d6aURYCRA")
    }
    if(message.content === "!gf magic")
    {
      let embed = new discord.MessageEmbed()
      .setTitle("IDIGO CHESESA MAGIC")
      .setDescription("Abrakadabra")
      .setColor("RANDOM")
      .setFooter("inkenti mari")
      message.channel.send({embeds:[embed]})
    }
     if(message.content === "!gf announce")
    {
      let embed = new discord.MessageEmbed()
      .setTitle("RTP Crypto Distribution!")
      .setDescription("Fill in the following Form with a valid Wallet Address and wait for random amount of RTP to be credited into your wallet! ")
      .setColor("RANDOM")
      .setFooter("")
      message.channel.send({embeds:[embed]})
      message.channel.send("Click here:\nhttps://forms.gle/rUxvCpwN4GvkGkrU6 ")
    }
    if(message.content === "!gf relli")
    {
      let embed = new discord.MessageEmbed()
      .setTitle("DANTE GADU RELLI")
      .setDescription("")
      .setColor("RANDOM")
      .setFooter("")
      message.channel.send({embeds:[embed]})
    }

    if(message.content === "!gf pro")
    {
      let embed = new discord.MessageEmbed()
      .setTitle("Risky's the Actual Pro")
      .setDescription("Follow his Facebook Page")
      .setColor("RANDOM")
      .setFooter("https://www.facebook.com/RiskyTrick.ftw")
      message.channel.send({embeds:[embed]})
    }
      if(message.content === "!gf living legend")
    {
      let embed = new discord.MessageEmbed()
      .setTitle("Bratike Bala krishna")
      .setDescription("")
      .setColor("RANDOM")
      .setFooter("when you fire the fire fire will be fired by the fire, im not the fire, im the Truth")
      message.channel.send({embeds:[embed]})
    }
    if(message.content === "!gf chapri")
    {
      let embed = new discord.MessageEmbed()
      .setTitle("ORIGINAL GADU CHAPRI")
      .setDescription("")
      .setColor("RANDOM")
      .setFooter("")
      message.channel.send({embeds:[embed]})
    }
    if(message.content === "!gf patchnotes"||message.content === "!gf patch notes")
    {
      let embed = new discord.MessageEmbed()
      .setTitle("Patch Notes")
      .setDescription("Recent updates:\n Running on discord.js V13\n new commands addes\ new daily gf tokens reedemable")
      .setColor("RANDOM")
      .setFooter("Contribute to creator, peace")
      message.channel.send({embeds:[embed]})
    }
    if(message.content === "!gf bengu")
    {
      let embed = new discord.MessageEmbed()
      .setTitle("ABBADALU CHEPTE ADDUKU TINTAV RA")
      .setDescription("")
      .setColor("RANDOM")
      .setFooter("")
      message.channel.send({embeds:[embed]})
    }
    if(message.content === "!gf mallibengu")
    {
      let embed = new discord.MessageEmbed()
      .setTitle("LIAR LIAR PANTS ON FIRE")
      .setDescription("")
      .setColor("RANDOM")
      .setFooter("")
     message.channel.send({embeds:[embed]})
    }
if(message.content === "!gf leki")
    {
      let embed = new discord.MessageEmbed()
      .setTitle("KUSHAL GADU LEKI")
      .setDescription("")
      .setColor("RANDOM")
      .setFooter("")
      message.channel.send({embeds:[embed]})
    }
    //=======================================MEME CODE

       if (message.content.startsWith('!gf meme')) {
   https.get(url, result => {
  var body = '';
  result.on('data', chunk => {
  body += chunk;
  });

  result
  .on('end', () => {
  var response = JSON.parse(body);
  var index =
  response.data.children[Math.floor(Math.random() * 99) + 1].data;

  var link = 'https://reddit.com/' + index.permalink;

  if (index.post_hint !== 'image') {
  var text = index.selftext;
  const textembed = new discord.MessageEmbed()
  .setTitle(`${title}`)
  .setColor('RANDOM')
  .setURL(link)

  message.channel.send({embeds:[textembed]})
  }
  var image = index.preview.images[0].source.url.replace('&amp;', '&');
 var title = index.title;
 var subRedditName = index.subreddit_name_prefixed;

 if (index.post_hint !== 'image') {
 const textembed = new discord.MessageEmbed()
 .setTitle(`${title}`)
 .setColor('RANDOM')
 .setURL(link);

 message.channel.send({embeds:[textembed]})
 }
 const imageembed = new discord.MessageEmbed()
 .setTitle(`${title}`)
 .setImage(image)
 .setColor('RANDOM')
 .setURL(link);
 message.channel.send({embeds:[imageembed]})
 })
 .on('error', function(e) {
 console.log('Got an error: ', e);
 });
 });
 }
 //==================== Spam test
  if(message.content.startsWith("!gf spam")) {
  let member = message.mentions.users.first()
  if(member.id === client.bot) return message.channel.send("you fool you dumb piece of shit why would you do this i trusted you")
  const user = message.mentions.users.first();
    if(!user) return message.channel.send("Mention Someone to Spam")
  setTimeout(() => {
  message.channel.send(`<@${user.id}>`+"Respond else get rekt")
  }, 2500);
  setTimeout(() => {
  message.channel.send(`<@${user.id}>`+"Answer ivvara bot")
  }, 3500);
  setTimeout(() => {
  message.channel.send(`<@${user.id}>`+"respond avvara bot")
  }, 4500);
  setTimeout(() => {
  message.channel.send(`<@${user.id}>`+"reply or die")
  }, 5500);
  setTimeout(() => {
  message.channel.send(`<@${user.id}>`+"answer iste 10 rs ista")
  }, 6500);
  setTimeout(() => {
  message.channel.send(`<@${user.id}>`+"mowa plis respond ra")
  }, 7500);
  setTimeout(() => {
  message.channel.send(`<@${user.id}>`+"5 rupee fivestar ista reply iste")
  }, 8500);
  setTimeout(() => {
  message.channel.send(`<@${user.id}>`+"mowa vro spam cheiyinchukovadam istama")
  }, 9500);
  setTimeout(() => {
  message.channel.send(`<@${user.id}>`+"not cool mowavro")
  }, 10500);
  setTimeout(() => {
  message.channel.send(`<@${user.id}>`+"inka na valla kadu, jaldi ra")
  }, 11500);
  setTimeout(() => {
  message.channel.send(`<@${user.id}>`+"ippudu respond avvale ante ne love story leak chesta")
  }, 12500);
  // message.channel.send(`<@${user.id}>`+"Respond else get rekt")
  // message.channel.send(`<@${user.id}>`+"Answer ivvara bot")
  // message.channel.send(`<@${user.id}>`+"respond avvara bot")
  // message.channel.send(`<@${user.id}>`+"reply or die")
  // message.channel.send(`<@${user.id}>`+"answer iste 10 rs ista")
  // message.channel.send(`<@${user.id}>`+"mowa plis respond ra")
  // message.channel.send(`<@${user.id}>`+"5 rupee fivestar ista reply iste")
  // message.channel.send(`<@${user.id}>`+"mowa vro spam cheiyinchukovadam istama")
  // message.channel.send(`<@${user.id}>`+"not cool mowavro")
  // message.channel.send(`<@${user.id}>`+"inka na valla kadu, jaldi ra")

  
  
}
 //=======================================END OF MEME CODE

    if(message.content.startsWith("!gf hack")) {
  let member = message.mentions.users.first()
  if(member.id === "your bot id") return message.channel.send("you fool you dumb piece of shit why would you do this i trusted you")
  const user = message.mentions.users.first();
    if(!user) return message.channel.send("Mention Someone to hack")
  message.channel.send("[25%] Finding IP..").then(m => {
  setTimeout(() => {
  m.edit("[50%] IP FOUND! Looking for email and password..").then(m2 => {
  setTimeout(() => {
  m2.edit(`**[75%]** DONE! email: ${user.username}@icloud.com | password: XjdhgikshGdk`).then(m3 => {
  setTimeout(() => {
  m3.edit("[100%] Deleting System32..").then(m4 => {
  setTimeout(() => {
  m4.edit(`done hacking ${user}! all info was Published online.`)
  }, 5500);
  });
  }, 2800);
  });
  }, 4500);
  });
  }, 5000);
  });
  };
  //==============================================================HELP
    if(message.content == "!gf help"||message.content ==="!gf commands"||message.content ==="!gf command"||message.content ==="!gf cmd") {
 let embed = new discord.MessageEmbed()
  .setTitle("Help commands (use !gf as prefix or else no work for you)")
  .setDescription("||Commands||  \n ping, troll, magic, pro,living legend, socials, divide, join,help,cmd,command,commands \n ||Fun|| \n relli, chapri, hack, leki, meme  \n ||secret command||\n google \n ||economy|| \n balance , deposit , bal , dep\n ||VOICE & MUSIC||\n play, skip , stop,ans,announce,announcement,music,record,playback\ndisconnect,dis \n ||Tournament Specific Commands|| \nRules , siege") 
   .setColor("RANDOM")
  .setFooter("TIP: If you need help you can dm @RiskyTrick.ftw", "https://cdn.discordapp.com/emojis/800581622910812170.gif?v=1")
  message.channel.send({embeds:[embed]})
}
    //=--==-=-===-=-=-===============================================

    
    if (message.content.toLowerCase().startsWith("!gf google")) 
 {
  let MSG = message.content.split(" ");
  let Query = MSG.slice(2).join("+");
  let QueryD = MSG.slice(2).join(" ");
  if (!Query) message.reply("Please specify a search query.")
  else
  {
    let GG = new discord.MessageEmbed()
    .setTitle(`Your Search Query: ${QueryD}`)
    .setDescription(`**Search Result** - [Click Here](https://www.google.com/search?q=${Query})`)
    .setColor("GREEN")
    .setFooter("There you go !")
    message.channel.send({embeds:[GG]})
  }
  }
  })
client.login("**TOKEN**")
