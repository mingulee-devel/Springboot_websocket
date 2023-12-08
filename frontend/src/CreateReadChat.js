import { useRef, useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import * as StompJs from '@stomp/stompjs';

function CreateReadChat() {
	const [chatList, setChatList] = useState([]);
	const [nick, setNick] = useState('');
	const [chat, setChat] = useState('');

	// const { apply_id } = useParams();
	const client = useRef({});

	useEffect(() => {
		console.log('chatList');
		console.log(chatList);
	}, [chatList]);

	useEffect(() => {
		console.log(client);
	}, [client]);

	const connect = () => {
		client.current = new StompJs.Client({
			brokerURL: 'ws://localhost:8620/ws',
			onConnect: () => {
				console.log('success');
				subscribe();
			},
			connectHeaders: { //jwt
				Authorization: nick,
				// Authorization: window.localStorage.getItem('authrization'),
			}
		});
		client.current.activate();
	};

	const publish = (chat) => {
		console.log('publish');
		if (!client.current.connected) return;

		console.log(nick);
		client.current.publish({
			destination: '/pub/chat',
			body: JSON.stringify({
				applyId: nick,
				// applyId: apply_id,
				channelId : 1,
				// writerId: 1,
				writerId: nick,
				chat: chat
			}),
		});

		setChat('');
	};

	const subscribe = () => {
		console.log('subscribe');
		client.current.subscribe('/sub/chat/' + nick, (body) => {
		// client.current.subscribe('/sub/chat/' + apply_id, (body) => {
			let json_body = JSON.parse(body.body);
			console.log(json_body);

			setChatList((_chat_list) => {
				console.log([
					..._chat_list, json_body
				]);
				return [
					..._chat_list, json_body
				]
			});
		});
	};

	const disconnect = () => {
		client.current.deactivate();
	};

	const handleNicChange = (event) => {
		setNick(event.target.value);
	};

	const handleChange = (event) => { // 채팅 입력 시 state에 값 설정
		setChat(event.target.value);
	};

	const handleSubmit = (event, chat) => { // 보내기 버튼 눌렀을 때 publish
		event.preventDefault();

		publish(chat);
	};

	useEffect(() => {
		window.localStorage.setItem('authrization', 'ming');
		console.log(window.localStorage.getItem('authrization'));

		connect();

		return () => disconnect();
	}, []);

	return (
		<div>
			<div className={'chat-list'}>{chatList.map(chat => <div>{chat.writerId +' : '+ chat.chat}</div>)}</div>
			<form onSubmit={(event) => handleSubmit(event, chat)}>
				<div>
					<input type={'text'} name={'nick'} onChange={handleNicChange} value={nick} />
					<input type={'text'} name={'chatInput'} onChange={handleChange} value={chat} />
				</div>
				<input type={'submit'} value={'의견 보내기'} />
			</form>
		</div>
	);
}

export default CreateReadChat;