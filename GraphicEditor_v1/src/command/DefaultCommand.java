package command;

import dto.GCommandDTO;

public class DefaultCommand implements GCommand<GCommandDTO> {

	@Override
	public void execute(GCommandDTO dto) {
		// 아무것도 하지않음
	}
}