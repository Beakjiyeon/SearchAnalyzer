package kr.co.tbase.searchad;

import kr.co.tbase.searchad.entity.Members;
import kr.co.tbase.searchad.repository.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCrypt;

@SpringBootApplication
public class AdanalyzeApplication  implements CommandLineRunner{//
	private final MemberRepository memberRepository;

	public AdanalyzeApplication(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	public static void main(String[] args) {

		SpringApplication.run(AdanalyzeApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {


		Members admin = new Members(Long.valueOf(1),"admin", BCrypt.hashpw("1234", BCrypt.gensalt()),"관리자", "admin");
		memberRepository.save(admin);

		admin = new Members(Long.valueOf(2),"a", BCrypt.hashpw("1234", BCrypt.gensalt()),"amy", "member");
		memberRepository.save(admin);
		admin = new Members(Long.valueOf(3),"aa", BCrypt.hashpw("1234", BCrypt.gensalt()),"bear", "member");
		memberRepository.save(admin);
		admin = new Members(Long.valueOf(4),"cccc", BCrypt.hashpw("1234", BCrypt.gensalt()),"elephant", "member");
		memberRepository.save(admin);
		admin = new Members(Long.valueOf(5),"bb", BCrypt.hashpw("1234", BCrypt.gensalt()),"myx", "member");
		memberRepository.save(admin);
		admin = new Members(Long.valueOf(6),"aaaaa", BCrypt.hashpw("1234", BCrypt.gensalt()),"멤버", "member");
		memberRepository.save(admin);
		admin = new Members(Long.valueOf(7),"abc", BCrypt.hashpw("1234", BCrypt.gensalt()),"멤버", "member");
		memberRepository.save(admin);
		admin = new Members(Long.valueOf(8),"abbc", BCrypt.hashpw("1234", BCrypt.gensalt()),"멤버", "member");
		memberRepository.save(admin);
		admin = new Members(Long.valueOf(9),"a3dmin", BCrypt.hashpw("1234", BCrypt.gensalt()),"멤버", "member");
		memberRepository.save(admin);
		admin= new Members(Long.valueOf(10),"asafn", BCrypt.hashpw("1234", BCrypt.gensalt()),"멤버", "member");
		memberRepository.save(admin);
		admin = new Members(Long.valueOf(11),"any", BCrypt.hashpw("1234", BCrypt.gensalt()),"멤버", "member");
		memberRepository.save(admin);
		admin = new Members(Long.valueOf(12),"jyo", BCrypt.hashpw("1234", BCrypt.gensalt()),"멤버", "member");
		memberRepository.save(admin);
		admin = new Members(Long.valueOf(13),"sisi", BCrypt.hashpw("1234", BCrypt.gensalt()),"멤버", "member");
		memberRepository.save(admin);
		admin = new Members(Long.valueOf(14), "ubrb", BCrypt.hashpw("1234", BCrypt.gensalt()), "멤버", "member");
		memberRepository.save(admin);
		admin = new Members(Long.valueOf(15),"aasffaaa", BCrypt.hashpw("1234", BCrypt.gensalt()),"멤버", "member");
		memberRepository.save(admin);
		admin = new Members(Long.valueOf(16),"aadfbc", BCrypt.hashpw("1234", BCrypt.gensalt()),"멤버", "member");
		memberRepository.save(admin);
		admin = new Members(Long.valueOf(17),"akkbbc", BCrypt.hashpw("1234", BCrypt.gensalt()),"멤버", "member");
		memberRepository.save(admin);
		admin = new Members(Long.valueOf(18),"a3dbgmin", BCrypt.hashpw("1234", BCrypt.gensalt()),"멤버", "member");
		memberRepository.save(admin);
		admin= new Members(Long.valueOf(19),"asammfn", BCrypt.hashpw("1234", BCrypt.gensalt()),"멤버", "member");
		memberRepository.save(admin);

	}


}


